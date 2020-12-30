package caseTwo

import java.io.File

import akka.actor.{ActorSystem, Props}
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration.DurationInt
import scala.io.StdIn

object Main {
  private val logger = Logger("Main")

  def main(args: Array[String]): Unit = {
    val system: ActorSystem = ActorSystem("SberTask")
    import system.dispatcher

    val mainActor = system.actorOf(Props[DirectoryHandler], "mainActor")

    val directory = StdIn.readLine()

    system.scheduler.scheduleAtFixedRate(0.seconds, 1.minute) {
      () => getListOfFiles(directory).foreach(mainActor ! _)
    }
  }

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists() && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List.empty[File]
    }
  }
}
