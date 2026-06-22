package DatenstrukturTupel_02

object DatenstrukturTupel_02 {
  def main(args: Array[String]): Unit = {
    val wetterListe = List(
      ("Zürich", 18.5),
      ("Bern", 21.0),
      ("Genf", 23.5),
      ("St. Gallen", 15.0)
    )

    val warmeStaedte = wetterListe.filter {
      case
        (stadt, temp) => temp > 20
    }

    println("Städte mit über 20 Grad:")
    warmeStaedte.foreach(s => println(s"${s._1}, ${s._2}"))
  }
}