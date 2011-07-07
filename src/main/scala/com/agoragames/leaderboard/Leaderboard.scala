package com.agoragames.leaderboard

import com.redis._

object LeaderboardDefaults {
    val VERSION = "1.0.0"
    val DEFAULT_PAGE_SIZE = 25
    val DEFAULT_REDIS_HOST = "localhost"
    val DEFAULT_REDIS_PORT = 6379   
}

class Leaderboard(leaderboardNameParam: String, host: String = LeaderboardDefaults.DEFAULT_REDIS_HOST, port: Int = LeaderboardDefaults.DEFAULT_REDIS_PORT, pageSizeParam: Int = LeaderboardDefaults.DEFAULT_PAGE_SIZE) {
    private val redisClient = new RedisClient(host, port)
    
    val leaderboardName: String = leaderboardNameParam
    var pageSize: Int = pageSizeParam
    val version = LeaderboardDefaults.VERSION
    
    if (pageSize < 1) {
        pageSize = LeaderboardDefaults.DEFAULT_PAGE_SIZE
    }
    
    def disconnect: Boolean = {
        redisClient.disconnect
    }
        
    def totalMembers: Option[Int] = {
        this.totalMembersIn(this.leaderboardName)
    }
        
    def totalMembersIn(leaderboardName: String): Option[Int] = {
        redisClient.zcard(leaderboardName)
    }

    def addMember(memberName: String, score: Double): Boolean = {
        this.addMemberTo(this.leaderboardName, memberName, score)
    }
    
    def addMemberTo(leaderboardName: String, memberName: String, score: Double): Boolean = {
        redisClient.zadd(leaderboardName, score, memberName)
    }
    
    def totalPages(leaderboardName: String = this.leaderboardName): Int = {
        this.totalPagesIn(leaderboardName, this.pageSize)
    }
    
    def totalPagesIn(leaderboardName: String, pageSize: Int): Int = {
        scala.math.ceil(this.totalMembersIn(leaderboardName).get.asInstanceOf[Float] / pageSize.asInstanceOf[Float]).asInstanceOf[Int]
    }

    // RedisClient does not currently support zcount.
    // def totalMembersInScoreRange(minScore: Double, maxScore: Double): Int = {
    //     this.totalMembersInScoreRangeIn(this.leaderboardName, minScore, maxScore)
    // }
    // 
    // def totalMembersInScoreRangeIn(leaderboardName: String, minScore: Double, maxScore: Double): Int = {
    //     redisClient.zcount(leaderboardName, minScore, maxScore)
    // }
    
    def scoreFor(member: String): Option[Double] = {
        this.scoreForIn(this.leaderboardName, member)
    }
    
    def scoreForIn(leaderboardName: String, member: String): Option[Double] = {
        redisClient.zscore(leaderboardName, member)
    }
    
    def changeScoreFor(member: String, score: Double): Option[Double] = {
        return changeScoreForIn(this.leaderboardName, member, score)
    }
    
    def changeScoreForIn(leaderboardName: String, member: String, score: Double): Option[Double] = {
        redisClient.zincrby(leaderboardName, score, member)
    }

    def checkMember(member: String): Boolean = {
        this.checkMemberIn(this.leaderboardName, member)
    }

    def checkMemberIn(leaderboardName: String, member: String): Boolean = {
        !(redisClient.zscore(leaderboardName, member) == None)
    }

    def rankFor(member: String, useZeroIndexForRank: Boolean = false): Option[Int] = {
        this.rankForIn(this.leaderboardName, member, useZeroIndexForRank)
    }
    
    def rankForIn(leaderboardName: String, member: String, useZeroIndexForRank: Boolean = false): Option[Int] = {
        if (useZeroIndexForRank) {
            redisClient.zrank(leaderboardName, member, true)            
        } else {
            // This feels "not elegant"
            Some(new java.lang.Integer(redisClient.zrank(leaderboardName, member, true).get + 1))
        }
    }
    
    def scoreAndRankFor(member: String, useZeroIndexForRank: Boolean = false): scala.collection.mutable.HashMap[String, Object] = {
        this.scoreAndRankForIn(this.leaderboardName, member, useZeroIndexForRank)
    }
    
    def scoreAndRankForIn(leaderboardName: String, member: String, useZeroIndexForRank: Boolean = false): scala.collection.mutable.HashMap[String, Object] = {
        val dataMap = scala.collection.mutable.HashMap.empty[String, Object]
        
        dataMap += ("member" -> member)
        dataMap += ("score" -> this.scoreForIn(leaderboardName, member))
        dataMap += ("rank" -> this.rankForIn(leaderboardName, member, useZeroIndexForRank))
        
        dataMap
    }

    def removeMembersInScoreRange(minScore: Double, maxScore: Double): Option[Int] = {
        this.removeMembersInScoreRangeIn(this.leaderboardName, minScore, maxScore)
    }
    
    def removeMembersInScoreRangeIn(leaderboardName: String, minScore: Double, maxScore: Double): Option[Int] = {
        redisClient.zremrangebyscore(leaderboardName, minScore, maxScore)
    }
    
    def leaders(currentPage: Int, withScores: Boolean = true, withRank: Boolean = true, useZeroIndexForRank: Boolean = false, pageSize: Int = LeaderboardDefaults.DEFAULT_PAGE_SIZE) = {
        this.leadersIn(this.leaderboardName, currentPage, withScores, withRank, useZeroIndexForRank, pageSize)
    }
    
    def leadersIn(leaderboardName: String, currentPageParam: Int, withScores: Boolean = true, withRank: Boolean = true, useZeroIndexForRank: Boolean = false, pageSize: Int = LeaderboardDefaults.DEFAULT_PAGE_SIZE): java.util.List[(String, Double, Int)] = {
        var currentPage: Int = currentPageParam
        
        if (currentPage < 1) {
            currentPage = 1
        }
        
        if (currentPage > totalPagesIn(leaderboardName, pageSize)) {
            currentPage = totalPagesIn(leaderboardName, pageSize)
        }
        
        var indexForRedis: Int = currentPage - 1
        var startingOffset: Int = (indexForRedis * pageSize)
        if (startingOffset < 0) {
            startingOffset = 0
        }
        var endingOffset: Int = (startingOffset + pageSize) + 1
        
        var rawLeaderData = redisClient.zrangeWithScore(leaderboardName, startingOffset, endingOffset, RedisClient.DESC)

        var massagedLeaderData: java.util.List[(String, Double, Int)] = new java.util.ArrayList[(String, Double, Int)]        
        if (rawLeaderData != None) {
            for (leader <- rawLeaderData.get) {
                massagedLeaderData.add((leader._1, leader._2, rankForIn(leaderboardName, leader._1, useZeroIndexForRank).get))
            }
        }
        
        massagedLeaderData
    }
}