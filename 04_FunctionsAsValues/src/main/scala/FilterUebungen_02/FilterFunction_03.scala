package FilterUebungen_02

object FilterFunction_03 {
  def main(args: Array[String]): Unit = {
    val numbers = List(12, 45, 68, 100)
    val bigNumbers = numbers.filter(_ > 50)
    println(bigNumbers)
  }
}