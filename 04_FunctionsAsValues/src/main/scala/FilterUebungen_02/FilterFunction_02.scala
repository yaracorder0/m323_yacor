package FilterUebungen_02

object FilterFunction_02 {
  def main(args: Array[String]): Unit = {
    val namen = List("Alice", "Bob", "Charlie", "Diana")
    val langeNamen = namen.filter(name => name.length > 4)
    println(langeNamen)
  }
}