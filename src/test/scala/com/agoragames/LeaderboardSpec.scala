package com.agoragames

import org.scalatest.Spec
import org.scalatest.BeforeAndAfterEach
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import com.redis._

@RunWith(classOf[JUnitRunner])
class LeaderboardSpec extends Spec 
                        with ShouldMatchers
                        with BeforeAndAfterEach
                        with BeforeAndAfterAll {

	val redisClient = new RedisClient("localhost", 6379)
    var leaderboard = new Leaderboard("leaderboard_name", "localhost", 6379, 25)

	override def beforeEach = {
		redisClient.flushdb
	}

	override def afterEach = {
	}

	override def afterAll = {
		redisClient.disconnect
		leaderboard.disconnect
	}
	
	private def addMembersToLeaderboard(totalMembers: Int) = {
		for (i <- 1 to totalMembers) {
			leaderboard.addMember("member_" + i, i)
		}
	}
	
    describe("version") {
      it("should be the correct version") {
          
          leaderboard.version should equal("1.0.0")
      }
    }
    
    describe("constructor") {
        it("should have the correct parameters") {
            leaderboard.leaderboardName should equal("leaderboard_name")
            leaderboard.pageSize should equal(25)
        }
    }
        
    describe("totalMembers and totalMembersIn") {
        it("should return the correct number of members for totalMembers") {
            leaderboard.totalMembers should equal(Some(0))
            leaderboard.totalMembers.get should equal(0)
        }
        
        it("should return the correct number of members for totalMembersIn") {
            leaderboard.totalMembersIn("leaderboard_name") should equal(Some(0))
            leaderboard.totalMembersIn("leaderboard_name").get should equal(0)
        }
    }
    
    describe("addMember and addMemberTo") {
        it("should be able to add a member to the leaderboard using addMember") {        
            leaderboard.addMember("member", 1337) should equal(true)
            leaderboard.totalMembersIn("leaderboard_name").get should equal(1)

            leaderboard.addMember("member", 1338) should equal(false)
            leaderboard.totalMembersIn("leaderboard_name").get should equal(1)
        }

        it("should be able to add a member to the leaderboard using addMemberTo") {
            leaderboard.addMemberTo("leaderboard_name", "member", 1337) should equal(true)
            leaderboard.totalMembersIn("leaderboard_name").get should equal(1)

            leaderboard.addMemberTo("leaderboard_name", "member", 1338) should equal(false)
            leaderboard.totalMembersIn("leaderboard_name").get should equal(1)

            leaderboard.addMemberTo("leaderboard_name", "another_member", 1339) should equal(true)
            leaderboard.totalMembersIn("leaderboard_name").get should equal(2)
         }
    }
    
    describe("totalPages and totalPagesIn") {
        it("should return the correct number of pages in the leaderboard using totalPages for a single page") {
            addMembersToLeaderboard(5)
            
            leaderboard.totalPages("leaderboard_name") should equal(1)
        }

        it("should return the correct number of pages in the leaderboard using totalPages") {
            addMembersToLeaderboard(LeaderboardDefaults.DEFAULT_PAGE_SIZE + 2)
            
            leaderboard.totalPages("leaderboard_name") should equal(2)
        }

        it("should return the correct number of pages in the leaderboard using totalPagesIn") {
            addMembersToLeaderboard(LeaderboardDefaults.DEFAULT_PAGE_SIZE + 2)
            
            leaderboard.totalPagesIn("leaderboard_name", LeaderboardDefaults.DEFAULT_PAGE_SIZE) should equal(2)
        }
    }
    
    // RedisClient does not currently support zcount.
    // describe("totalMembersInScoreRange and totalMembersInScoreRangeIn") {
    //     it("should return correct number of members for totalMembersInScoreRange") {
    //         addMembersToLeaderboard(5)
    //         
    //         leaderboard.totalMembersInScoreRange(2, 4) should equal(3)
    //     }
    // }
    
    describe("scoreFor and scoreforIn") {
        it("should return the correct score for a member using scoreFor") {
            addMembersToLeaderboard(5)
            
            leaderboard.scoreFor("member_3").get should equal(3.0)
        }

        it("should return the correct score for a member using scoreForIn") {
            addMembersToLeaderboard(5)
            
            leaderboard.scoreForIn("leaderboard_name", "member_3").get should equal(3.0)
        }
    }

    describe("changeScoreFor and changeScoreforIn") {
        it("should return the correct score for a member using changeScoreFor") {
            addMembersToLeaderboard(5)
            
            leaderboard.changeScoreFor("member_3", 6).get should equal(9.0)
            leaderboard.changeScoreFor("member_3", -3).get should equal(6.0)
        }

        it("should return the correct score for a member using changeScoreForIn") {
            addMembersToLeaderboard(5)
            
            leaderboard.changeScoreForIn("leaderboard_name", "member_3", 6).get should equal(9.0)
            leaderboard.changeScoreForIn("leaderboard_name", "member_3", -3).get should equal(6.0)
        }
    }
    
    describe("checkMember and checkMemberIn") {
        it("should return whether or not a member is in the leaderboard using checkMember") {
            addMembersToLeaderboard(5)

            leaderboard.totalMembers.get should equal(5)
            leaderboard.checkMember("member_3") should equal(true)
            leaderboard.checkMember("member_10") should equal(false)
        }

        it("should return whether or not a member is in the leaderboard using checkMemberIn") {
            addMembersToLeaderboard(5)
            
            leaderboard.checkMemberIn("leaderboard_name", "member_3") should equal(true)
            leaderboard.checkMemberIn("leaderboard_name", "member_10") should equal(false)
        }
    }
    
    describe("rankFor") {
        it("should return the correct rank for rankFor") {
            addMembersToLeaderboard(5)
            
            leaderboard.rankFor("member_4").get should equal(2)
            leaderboard.rankFor("member_4", true).get should equal(1)
        }
    }
    
    describe("scoreAndRankFor") {
        it("should return the correct rank and score for scoreAndRankFor") {
            addMembersToLeaderboard(5)
            
            val dataMap: scala.collection.mutable.HashMap[String, Object] = leaderboard.scoreAndRankFor("member_1")
            
            dataMap("member") should equal("member_1")
            dataMap("score").asInstanceOf[Option[Double]].get should equal(1.0)
            dataMap("rank").asInstanceOf[Option[Int]].get should equal(5)
        }
    }
}