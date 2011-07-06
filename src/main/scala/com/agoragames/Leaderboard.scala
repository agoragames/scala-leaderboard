package com.agoragames

import com.redis._

object LeaderboardDefaults {
    val VERSION = "1.0.0"
	val DEFAULT_PAGE_SIZE = 25
	val DEFAULT_REDIS_HOST = "localhost"
	val DEFAULT_REDIS_PORT = 6379
	
}

class Leaderboard(leaderboardNameParam: String, host: String, port: Int, pageSizeParam: Int) {
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

    def addMember(memberName:String, score: Double): Boolean = {
        this.addMemberTo(this.leaderboardName, memberName, score)
    }
    
    def addMemberTo(leaderboardName:String, memberName: String, score: Double): Boolean = {
        redisClient.zadd(leaderboardName, score, memberName)
    }
    
    def totalPages(leaderboardName:String): Int = {
        this.totalPagesIn(this.leaderboardName, this.pageSize)
    }
    
    def totalPagesIn(leaderboardName:String, pageSize: Int): Int = {
		scala.math.ceil(this.totalMembersIn(leaderboardName).get.asInstanceOf[Float] / pageSize.asInstanceOf[Float]).asInstanceOf[Int]
    }
}