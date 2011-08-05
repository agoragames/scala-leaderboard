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
	
Usage
============

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

