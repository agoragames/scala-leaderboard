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

