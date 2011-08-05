scala-leaderboard
=================

Leaderboards backed by Redis in Scala, http://redis.io.

Builds off ideas proposed in http://blog.agoragames.com/2011/01/01/creating-high-score-tables-leaderboards-using-redis/.

Installation
============

Make sure your redis server is running! Redis configuration is outside the scope of this README, but 
check out the Redis documentation, http://redis.io/documentation.

scala-leaderboard uses Scala Build Tool, https://github.com/harrah/xsbt/wiki, for building and testing. Clone the source from GitHub and build using:

    dczarnecki-agora:scala-leaderboard dczarnecki$ sbt package
    [info] Set current project to default (in build file:/Users/dczarnecki/projects/scala-leaderboard/)
    [info] Packaging /Users/dczarnecki/projects/scala-leaderboard/target/scala-2.9.0.final/scala-leaderboard_2.9.0-2.0.0.jar ...
    [info] Done packaging.
    [success] Total time: 1 s, completed Aug 5, 2011 9:20:44 AM  

Testing
=======

You can run the tests using:

    sbt test

Compatibility
============

The project has been developed using Scala version 2.9.0.1 and Scala Build Tool version 0.10

Usage
============

Create a new leaderboard or attach to an existing leaderboard named 'highscores':

    scala> var highscore_lb = new Leaderboard("highscores", "localhost", 6379, 25)
    leaderboard: com.agoragames.leaderboard.Leaderboard = com.agoragames.leaderboard.Leaderboard@2f8d604f
    
Add members to your leaderboard using rankMember:

    scala> for (i <- 1 to 10) {
         | highscore_lb.rankMember("member_" + i, i)
         | }
    
You can call rankMember with the same member and the leaderboard will be updated automatically.

Get some information about your leaderboard:

    scala> highscore_lb.totalMembers
    res4: Option[Int] = Some(10)
    
    scala> highscore_lb.totalPages
    res5: Int = 1
    
Get some information about a specific member(s) in the leaderboard:

    scala> highscore_lb.scoreFor("member_4")
    res6: Option[Double] = Some(4.0)

    scala> highscore_lb.rankFor("member_4")
    res7: Option[Int] = Some(7)

    scala> highscore_lb.rankFor("member_10")
    res8: Option[Int] = Some(1)
    
Get page 1 in the leaderboard:

    scala> highscore_lb.leaders(1)
    res9: java.util.List[(String, Double, Int)] = [(member_10,10.0,1), (member_9,9.0,2), (member_8,8.0,3), (member_7,7.0,4), (member_6,6.0,5), (member_5,5.0,6), (member_4,4.0,7), (member_3,3.0,8), (member_2,2.0,9), (member_1,1.0,10)]
    
Add more members to your leaderboard:

    scala> for (i <- 50 to 95) {
         | highscore_lb.rankMember("member_" + i, i)
         | }

    scala> highscore_lb.totalPages
    res11: Int = 3
    
Get an "Around Me" leaderboard for a member:

    scala> highscore_lb.aroundMe("member_53")
    res13: java.util.List[(String, Double, Int)] = [(member_65,65.0,31), (member_64,64.0,32), (member_63,63.0,33), (member_62,62.0,34), (member_61,61.0,35), (member_60,60.0,36), (member_59,59.0,37), (member_58,58.0,38), (member_57,57.0,39), (member_56,56.0,40), (member_55,55.0,41), (member_54,54.0,42), (member_53,53.0,43), (member_52,52.0,44), (member_51,51.0,45), (member_50,50.0,46), (member_10,10.0,47), (member_9,9.0,48), (member_8,8.0,49), (member_7,7.0,50), (member_6,6.0,51), (member_5,5.0,52), (member_4,4.0,53), (member_3,3.0,54), (member_2,2.0,55)]
    
Get rank and score for an arbitrary list of members (e.g. friends):

    scala> highscore_lb.rankedInList(Array("member_1", "member_62", "member_67"))
    res19: java.util.List[(String, Double, Int)] = [(member_1,1.0,56), (member_62,62.0,34), (member_67,67.0,29)]
    
Other useful methods:

    deleteLeaderboard: Delete the current leaderboard  
    removeMember(member): Remove a member from the leaderboard
    totalMembers: Total # of members in the leaderboard
    totalPages: Total # of pages in the leaderboard given the leaderboard's page_size	
    changeScoreFor(member, score): Change the score for a member by some amount delta (delta could be positive or negative)
    rankFor(member): Retrieve the rank for a given member in the leaderboard
    scoreFor(member): Retrieve the score for a given member in the leaderboard
    checkMember(member): Check to see whether member is in the leaderboard
    scoreAndRankFor(member): Retrieve the score and rank for a member in a single call
    removeMembersInScoreRange(minScore, maxScore): Remove members from the leaderboard within a score range
    
Future Ideas
============
  
Contributing to scala-leaderboard
=================================
 
* Check out the latest master to make sure the feature hasn't been implemented or the bug hasn't been fixed yet
* Check out the issue tracker to make sure someone already hasn't requested it and/or contributed it
* Fork the project
* Start a feature/bugfix branch
* Commit and push until you are happy with your contribution
* Make sure to add tests for it. This is important so I don't break it in a future version unintentionally.
* Please try not to mess with the build files, version, or history. If you want to have your own version, or is otherwise necessary, that is fine, but please isolate to its own commit so I can cherry-pick around it.

Copyright
============

Copyright (c) 2011 David Czarnecki. See LICENSE.txt for further details.

