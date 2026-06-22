package DatenstrukturTupel_02

import java.time.LocalTime

object DatenstrukturTupel_01 {
  def main(args: Array[String]): Unit = {
    def wetterFunktion(beschreibung: String, temperatur: Double): (String, LocalTime, Double) = {
      val zeit = LocalTime.now()
      (beschreibung, zeit, temperatur)
    }

    val standortInfo = wetterFunktion("sonnig", 22.5)
    println(s"Wetter: ${standortInfo._1}, Zeit: ${standortInfo._2}, Temp: ${standortInfo._3}")
  }
}