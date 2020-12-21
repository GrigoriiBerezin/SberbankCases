package caseThree

import scala.io.StdIn

object CaseThree extends App {
  // Better to use Vector instead of List due to get by index
  type Row = List[Int]
  type Matrix = List[Row]

  type Result = Either[String, Int]

  private def createRow(matrix: Matrix): Matrix = {
    StdIn.readLine().split(" ").toList.map(_.toInt) match {
      case head :: tail =>
        if (tail.length == head) matrix :+ tail
        else throw new Exception("The length of line doesn't equal to initial size!")
      case head :: Nil =>
        if (head == 0) matrix :+ List.empty
        else throw new Exception("The line contains only initial size!")
      case Nil => throw new Exception("Unexpected end of input!")
    }
  }

  private def initMatrix: Matrix = Range(0, StdIn.readInt()).foldLeft(List.empty[Row])((acc, _) => createRow(acc))

  private def getValues(matrix: Matrix): List[Result] = {
    def getValue: Result =
      StdIn.readLine().split(" ").toList.map(_.toInt) match {
        case row :: column :: Nil => matrix.lift(row - 1).flatMap(_.lift(column - 1)).toRight("ОШИБКА!")
        case _ => throw new Exception("Incorrect format!")
      }

    Range(0, StdIn.readInt()).foldLeft(List.empty[Result])((acc, _) => acc :+ getValue)
  }

  private val matrix: Matrix = initMatrix
  println(matrix)
  getValues(matrix).foreach {
    case Left(error) => println(error)
    case Right(result) => println(result)
  }
}
