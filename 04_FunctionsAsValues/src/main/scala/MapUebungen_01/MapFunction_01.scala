package MapUebungen_01

object MapFunction_01 {
  def main(args: Array[String]) : Unit = {
    // Create scala-list
    val numbers = List(1, 2, 3, 4, 5)

    // Use map function
    val doubled = numbers.map(x => x * 2)

    // Print result
    println(doubled)
  }
}