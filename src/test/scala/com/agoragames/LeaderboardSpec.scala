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

	override def beforeEach = {
	}

	override def afterEach = {
		redisClient.flushdb
	}

	override def afterAll = {
		redisClient.disconnect
	}
	
    describe("version") {
      it("should be the correct version") {
          var leaderboard = new Leaderboard("leaderboard", "localhost", 6379, 25)
          
          leaderboard.version should equal("1.0.0")
      }
    }
    
    describe("constructor") {
        it("should have the correct parameters") {
            var leaderboard = new Leaderboard("leaderboard_name", "localhost", 6379, 25)
            
            leaderboard.leaderboardName should equal("leaderboard_name")
            leaderboard.pageSize should equal(25)
        }
    }
}