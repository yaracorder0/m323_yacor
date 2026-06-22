package MapUebungen_01

object MapFunction_02 {
  def main(args: Array[String]) : Unit = {
    val names = List("Alice", "Bob", "Charlie")
    val upperNames = names.map(name => name.toUpperCase)
    println(upperNames)
  }
}