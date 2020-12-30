package caseTwo

import java.io.File

import akka.actor.{Actor, ActorLogging}

class DirectoryHandler extends Actor with ActorLogging {
  private val formatters: List[Formatter] = List(XmlFormatter, JsonFormatter)
  private val defaultFormatter: Formatter = DefaultFormatter

  override def receive: Receive = {
    case file: File =>
      findAndChangeBehaviour(file, List.empty[File])
  }

  def ignoreCheckedFiles(file: File, checkedFiles: List[File]): Receive = {
    case newFile: File =>
      val newCheckedFiles = file :: checkedFiles
      findAndChangeBehaviour(newFile, newCheckedFiles)
  }

  private def findAndChangeBehaviour(file: File, checkedFiles: List[File]): Unit = {
    val suitableFormatter = formatters.find(_.isValidFormat(file.getName)).getOrElse(defaultFormatter)
    suitableFormatter.process(file)
    context.become(ignoreCheckedFiles(file, checkedFiles))
  }
}

