package FilterUebungen_02

object FilterFunction_05 {
  def main(args: Array[String]): Unit = {
    case class Buch(titel: String, autor: String, jahr: Int)

    val buecher = List(
      Buch("1984", "George Orwell", 1949),
      Buch("Brave New World", "Aldous Huxley", 1932),
      Buch("Fahrenheit 451", "Ray Bradbury", 1953)
    )

    val result = buecher.filter(_.jahr < 1950).map(_.titel)
    println(result)
  }
}