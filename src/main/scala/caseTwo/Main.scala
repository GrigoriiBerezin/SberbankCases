package caseTwo

import java.io.File

import com.typesafe.scalalogging.Logger

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.io.StdIn


object Main extends App {
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists() && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      logger.info(s"There is no files in directory: ${d.getAbsolutePath}")
      List.empty[File]
    }
  }

  private var handledFiles = List.empty[String]
  private val handlers: Seq[FormatHandler] = Seq(XmlFormatHandler, JsonFormatHandler)
  private val handlersMap = Map(handlers map { h => h.validFormat -> h }: _*)
  private val logger = Logger("Main")

  private val directory = StdIn.readLine() // your directory to monitoring

  def backgroundFuture: Future[Unit] = Future {
    getListOfFiles(directory).foreach(file => {
      if (!handledFiles.contains(file.getName)) {
        handledFiles = file.getName :: handledFiles // not functional but don't have enough time for thinking
        handlersMap.getOrElse(file.getName.split("\\.").last, DefaultFormatHandler).process(file)
      }
    } )
  }

  @tailrec
  def startProcess(): Unit = {
    Await.result(backgroundFuture, 10.seconds)
    startProcess()
  }

  startProcess()
}
