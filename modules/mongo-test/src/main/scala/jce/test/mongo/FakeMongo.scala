package jce.test.mongo

import de.flapdoodle.embed.mongo.{MongodExecutable, MongodProcess, MongodStarter}
import de.flapdoodle.embed.mongo.config.{IMongoCmdOptions, MongodConfigBuilder, Net}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network

class FakeMongo(val config: FakeMongoConfig) {
  import config._

  private var mongodExe: MongodExecutable = _
  private var mongodProcess: MongodProcess = _

  def start(): Unit = {
    mongodExe = prepareExe()
    mongodProcess = mongodExe.start()
  }

  def stop(): Unit = {
    mongodProcess.stop()
    mongodExe.stop()
  }

  private def prepareExe(): MongodExecutable = {
    val builder = new MongodConfigBuilder()
      .version(mongoVersion)
      .net(new Net(port, Network.localhostIsIPv6()))
    cmdOptions.map(o => builder.cmdOptions(o))
    params.foreach { case (k, v) => builder.setParameter(k, v)}
    MongodStarter.getDefaultInstance.prepare(builder.build())
  }
}

case class FakeMongoConfig(
  port: Int,
  mongoVersion: Version = Version.V3_2_1,
  cmdOptions: Option[IMongoCmdOptions]= None,
  params: Map[String, String] = Map()
)
