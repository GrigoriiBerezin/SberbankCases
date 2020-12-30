package caseTwo

import java.io.File
import java.nio.file.Files

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait Formatter {
  protected def logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  def validFormat: String

  def process(file: File): Future[Unit] = Future {
    if (isValidFormat(file.getName)) {
      logger.info(s"Working on ${file.getName} file")
      logger.info(s"${file.getName} has ${Files.lines(file.toPath).count()} lines")
    }
  }

  def isValidFormat(fileName: String): Boolean = fileName.endsWith("." + validFormat)
}

case object XmlFormatter extends Formatter {
  override def validFormat: String = "xml"
}

case object JsonFormatter extends Formatter {
  override def validFormat: String = "json"
}

case object DefaultFormatter extends Formatter {
  override def validFormat: String = ""

  override def process(file: File): Future[Unit] = Future {
    logger.error(s"Failed to delete ${file.getName}")
  }

  override def isValidFormat(fileName: String): Boolean = true
}
