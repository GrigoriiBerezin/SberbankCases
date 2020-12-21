package caseTwo

import java.io.File

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

sealed trait FormatHandler {
  protected def logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  def validFormat: String

  def process(file: File): Future[Unit] = Future {
    if (isValidFormat(file.getName)) logger.debug(s"Working on ${file.getName} file")
  }

  def isValidFormat(fileName: String): Boolean = fileName.endsWith("." + validFormat)
}

case object XmlFormatHandler extends FormatHandler {
  override def validFormat: String = ".xml"
}

case object JsonFormatHandler extends FormatHandler {
  override def validFormat: String = ".json"
}

case object DefaultFormatHandler extends FormatHandler {
  override def process(file: File): Future[Unit] = Future {
//    if (file.delete()) logger.debug(s"File ${file.getName} was deleted successfully")
//    else logger.error(s"Failed to delete ${file.getName}")
    logger.error(s"Failed to delete ${file.getName}")
  }

  override def validFormat: String = ""

  override def isValidFormat(fileName: String): Boolean = false
}
