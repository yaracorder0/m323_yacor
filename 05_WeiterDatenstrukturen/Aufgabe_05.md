# Aufgabe 05 - Datenstrukturen (Maps und Tupel)

In dieser Aufgabe wurden die Datenstrukturen `Map` und `Tuple` in Scala vertieft.

## 01 Datenstruktur Map

Eine `Map` ist eine Sammlung von Schlüssel-Wert-Paaren, bei der jeder Schlüssel eindeutig ist.

### DatenstrukturMap
In dieser Übung werden grundlegende Operationen auf Maps durchgeführt: Erstellen, Hinzufügen, Aktualisieren, Löschen und Abfragen von Werten.
```scala
package DatenstrukturMap_01

object DatenstrukturMap {
  def main(args: Array[String]): Unit = {
    // Aufgabe 1: Erstellen einer Map
    val m1: Map[String, String] = Map("key" -> "value")
    println(m1)

    // Aufgabe 2: Hinzufügen eines Elements
    val m2 = m1 + ("key2" -> "value2")
    println(m2)

    // Aufgabe 3: Aktualisieren eines Werts (da Maps immutable sind, wird eine neue Map erstellt)
    val m3 = m2 + ("key2" -> "aDifferentValue")
    println(m3)

    // Aufgabe 4: Entfernen eines Schlüssels
    val m4 = m3 - "key"
    println(m4)

    // Aufgabe 5: Sicherer Zugriff auf einen vorhandenen Schlüssel mit Option
    val valueFromM3: Option[String] = m3.get("key")
    println(valueFromM3)

    // Aufgabe 6: Sicherer Zugriff auf einen nicht vorhandenen Schlüssel
    val valueFromM4: Option[String] = m4.get("key")
    println(valueFromM4)
  }
}
```

## 02 Datenstruktur Tupel

Tupel erlauben es, mehrere Werte unterschiedlichen Typs in einer einzigen Struktur zusammenzufassen.

### DatenstrukturTupel_01
Verwendung eines Tupels als Rückgabewert einer Funktion, um mehrere Informationen (Beschreibung, Zeit, Temperatur) gleichzeitig zurückzugeben.
```scala
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
```

### DatenstrukturTupel_02
Filtern einer Liste von Tupeln. Hier werden Städte extrahiert, deren Temperatur über 20 Grad liegt.
```scala
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
```

### DatenstrukturTupel_03
Verwendung von `zip`, um aufeinanderfolgende Elemente einer Liste in Tupeln zu paaren, um einen Trend (stetig steigende Werte) zu prüfen.
```scala
package DatenstrukturTupel_02

object DatenstrukturTupel_03 {
  def main(args: Array[String]): Unit = {
    def trending(rates: List[BigDecimal]): Boolean = {
      if (rates.size < 2) return true

      val pairs = rates.zip(rates.drop(1))

      pairs.forall {
        case
          (prev, curr) => curr > prev
      }
    }
    println(s"Trend 1, 4, 3, 8: ${trending(List(1, 4, 3, 8))}")
    println(s"Trend 1, 2, 3, 8: ${trending(List(1, 2, 3, 8))}")
  }
}
```