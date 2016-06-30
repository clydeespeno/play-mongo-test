Integration Testing with Play and Reactivemongo
===========

**Play Scala with ReactiveMongo using Scalatest**

This repository contains opinionated approach on integration tests with play that uses mongodb. The currently supported
driver is [reactivemongo](http://reactivemongo.org/).
 
* **Scalatest** - One of the most popular scala testing frameworks. Designed to be simple and efficient.
  * [Scalatest Docs](http://www.scalatest.org/)
  
* **PlayReactiveMongo** - a helper library for play on top of reactivemongo to ease translation from Json to Bson.
  * [Play-ReactiveMongo github](https://github.com/ReactiveMongo/Play-ReactiveMongo)
  
* **Embedded Mongo**  - a simple library to spawn mongodb processes easily, very helpful for integration tests with mongodb.
  * [Embedded Mongo](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)
  
Sample Usage
===========
You may check the [example play application](https://github.com/clydeespeno/play-mongo-test/tree/master/play-test-app) on how 
to use reactivemongo. More examples will be added later on. You can skip to the [example tests](https://github.com/clydeespeno/play-mongo-test/tree/master/play-test-app/test) to see some test examples,

The sample application tests may be run with the ff:

    > sbt
    > project playTestApp
    > test

Each of the project with the exception of the test application could be imported separately in your project. 

Building
===========
There are no public repositories that hosts the binary for the test utilities. You may build the artifacts and publish locally 
or to your own public/private repositories.
  
After checking out / cloning this repository:


    > sbt publishLocal
    
or

    > sbt -Drepo=yourRepo publish
