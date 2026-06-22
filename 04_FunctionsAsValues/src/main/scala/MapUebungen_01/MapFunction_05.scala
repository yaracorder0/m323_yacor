package MapUebungen_01

object MapFunction_05 {
  def main(args: Array[String]): Unit = {
    val namen = List("Max Mustermann", "Erika Mustermann")
    val upperFirstNames = namen.map(firstname => firstname.split(" ")(0).toUpperCase)
    println(upperFirstNames)
  }
}