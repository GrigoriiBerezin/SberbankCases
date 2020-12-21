package caseTwo

import java.io.File

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.StdIn


object Main extends App {
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists() && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List.empty[File]
    }
  }

  private val handlers: Seq[FormatHandler] = Seq(XmlFormatHandler, JsonFormatHandler)
  private val handlersMap = Map(handlers map { h => h.validFormat -> h }: _*)
  private val directory = StdIn.readLine() // your directory to monitoring

  def backgroundFuture: Future[Unit] = Future {
    getListOfFiles(directory).foreach(file => handlersMap.getOrElse(file.getName.split("\\.").last, DefaultFormatHandler).process(file))
  }

  def startProcess(): Unit = {
    backgroundFuture.map(_ => backgroundFuture)
  }

  getListOfFiles(directory).foreach(file => handlersMap.getOrElse(file.getName.split("\\.").last, DefaultFormatHandler).process(file))
}
