package com.agoragames

import com.redis._

object LeaderboardDefaults {
    val VERSION = "1.0.0"
	val DEFAULT_PAGE_SIZE = 25
	val DEFAULT_REDIS_HOST = "localhost"
	val DEFAULT_REDIS_PORT = 6379
	
}

class Leaderboard(leaderboardNameParam:String, host: String, port: Int, pageSizeParam: Int) {
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
}