# Aufgabe 04 - Scala Übungen

In dieser Aufgabe wurden verschiedene funktionale Programmierkonzepte in Scala geübt, darunter `map`, `filter`, `foldLeft`, `flatMap` und `for-comprehensions`.

## 01 Map Übungen

Die `map`-Funktion wird verwendet, um jedes Element einer Kollektion zu transformieren.

### MapFunction_01
Verdoppelt jede Zahl in einer Liste.
```scala
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
```

### MapFunction_02
Wandelt eine Liste von Namen in Grossbuchstaben um.
```scala
package MapUebungen_01

object MapFunction_02 {
  def main(args: Array[String]) : Unit = {
    val names = List("Alice", "Bob", "Charlie")
    val upperNames = names.map(name => name.toUpperCase)
    println(upperNames)
  }
}
```

### MapFunction_03
Halbiert alle Zahlen in einer Liste.
```scala
package MapUebungen_01

object MapFunction_03 {
  def main(args: Array[String]): Unit = {
    val numbers = List(12, 45, 68, 100)
    val divided = numbers.map(number => number / 2)
    println(divided)
  }
}
```

### MapFunction_04
Transformiert eine Liste von `Adresse`-Objekten in eine Liste von formatierten Strings.
```scala
package MapUebungen_01

object MapFunction_04 {
  def main(args: Array[String]): Unit = {
    case class Adresse(strasse: String, hausnummer: Int, postleitzahl: String, stadt: String)

    val adressen = List(
      Adresse("Hauptstrasse", 10, "12345", "Musterstadt"),
      Adresse("Nebenstrasse", 5, "23456", "Beispielburg")
    )

    val formatted = adressen.map(adresse => s"${adresse.strasse}, ${adresse.hausnummer}, ${adresse.postleitzahl}, ${adresse.stadt}")
    println(formatted)
  }
}
```

### MapFunction_05
Extrahiert den Vornamen aus einem vollen Namen und wandelt diesen in Grossbuchstaben um.
```scala
package MapUebungen_01

object MapFunction_05 {
  def main(args: Array[String]): Unit = {
    val namen = List("Max Mustermann", "Erika Mustermann")
    val upperFirstNames = namen.map(firstname => firstname.split(" ")(0).toUpperCase)
    println(upperFirstNames)
  }
}
```

## 02 Filter Übungen

Die `filter`-Funktion behält nur die Elemente einer Kollektion, die eine bestimmte Bedingung erfüllen.

### FilterFunction_01
Filtert alle geraden Zahlen aus einer Liste.
```scala
package FilterUebungen_02

object FilterFunction_01 {
  def main(args: Array[String]): Unit = {
    val numbers = List(1, 2, 3, 4, 5)
    val evenNumbers = numbers.filter(number => number % 2 == 0)
    println(evenNumbers)
  }
}
```

### FilterFunction_02
Behält nur Namen, die länger als 4 Zeichen sind.
```scala
package FilterUebungen_02

object FilterFunction_02 {
  def main(args: Array[String]): Unit = {
    val namen = List("Alice", "Bob", "Charlie", "Diana")
    val langeNamen = namen.filter(name => name.length > 4)
    println(langeNamen)
  }
}
```

### FilterFunction_03
Filtert Zahlen, die grösser als 50 sind.
```scala
package FilterUebungen_02

object FilterFunction_03 {
  def main(args: Array[String]): Unit = {
    val numbers = List(12, 45, 68, 100)
    val bigNumbers = numbers.filter(_ > 50)
    println(bigNumbers)
  }
}
```

### FilterFunction_04
Behält nur Wörter, die mit dem Buchstaben "S" beginnen.
```scala
package FilterUebungen_02

object FilterFunction_04 {
  def main(args: Array[String]): Unit = {
    val words = List("Scala", "ist", "fantastisch")
    val sWords = words.filter(_.startsWith("S"))
    println(sWords)
  }
}
```

### FilterFunction_05
Filtert eine Liste von Büchern nach dem Erscheinungsjahr (vor 1950) und gibt deren Titel zurück.
```scala
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
```

## 03 Map und Filter Kombinationen

### MapFilterFunction_01
Filtert Mitarbeiter der IT-Abteilung mit einem Gehalt von mindestens 50'000 und gibt deren Vornamen in Grossbuchstaben zurück.
```scala
package MapUndFilterUebung_03

object MapFilterFunction_01 {
  def main(args: Array[String]): Unit = {
    case class Mitarbeiter(name: String, abteilung: String, gehalt: Int)

    val mitarbeiter = List(
      Mitarbeiter("Max Mustermann", "IT", 50000),
      Mitarbeiter("Erika Musterfrau", "Marketing", 45000),
      Mitarbeiter("Klaus Klein", "IT", 55000),
      Mitarbeiter("Julia Gross", "HR", 40000)
    )
    val result = mitarbeiter.filter(m => m.abteilung == "IT" && m.gehalt >= 50000 ).map(m => m.name.split(" ")(0).toUpperCase)
    println(result)
  }
}
```

### MapFilterFunction_02
Filtert Kurse, die "Daten" im Namen enthalten, entfernt Leerzeichen und gibt sie sortiert aus.
```scala
package MapUndFilterUebung_03

object MapFilterFunction_02 {
  def main(args: Array[String]): Unit = {
    val kurse = List(
      "Programmierung in Scala",
      "Datenbanken",
      "Webentwicklung mit JavaScript",
      "Algorithmen und Datenstrukturen"
    )
    val bearbeiteteKurse = kurse.filter(k => k.contains("Daten")).map(k => k.replace(" ", ""))

    val alphabetisch = bearbeiteteKurse.sorted

    val umgekehrt = bearbeiteteKurse.sorted.reverse

    println(s"Alphabetisch: $alphabetisch")
    println(s"Umgekehrt: $umgekehrt")
  }
}
```

## 04 FoldLeft Übungen

`foldLeft` kombiniert alle Elemente einer Liste zu einem einzigen Wert unter Verwendung eines Startwerts und einer Akkumulator-Funktion.

### FoldLeftFunction_01
Berechnet die Summe aller Zahlen in einer Liste.
```scala
package FoldLeftUebungen_04

object FoldLeftFunction_01 {
  def main(args: Array[String]): Unit = {
    val zahlen = List(1, 2, 3, 4, 5)
    val summe = zahlen.foldLeft(0)((acc, n) => acc + n)
    println(summe)
  }
}
```

### FoldLeftFunction_02
Verbindet eine Liste von Strings zu einem einzigen Satz.
```scala
package FoldLeftUebungen_04

object FoldLeftFunction_02 {
  def main(args: Array[String]): Unit = {
    val textTeile = List("Hello", " ", "Welt", "!")
    val kombiniert = textTeile.foldLeft("")((acc, wort) => acc + wort)
    println(kombiniert)
  }
}
```

### FoldLeftFunction_03
Berechnet den geometrischen Schwerpunkt (Mittelwert der X- und Y-Koordinaten) aus einer Liste von Punkten.
```scala
package FoldLeftUebungen_04

object FoldLeftFunction_03 {
  def main(args: Array[String]): Unit = {
    val points = List((1, 3), (2, 5), (4, 8), (6, 2))

    val sumTuple = points.foldLeft((0, 0)) { (acc, point) =>
      val sumX = acc._1 + point._1
      val sumY = acc._2 + point._2
      (sumX, sumY)
    }

    val n = points.length
    val schwerpunkt = (sumTuple._1.toDouble / n, sumTuple._2.toDouble / n)

    println(s"Schwerpunkt: $schwerpunkt")
  }
}
```

## 05 FlatMap Übungen

`flatMap` wendet eine Funktion an, die eine Kollektion zurückgibt, und flacht das Ergebnis anschliessend ab.

### FlatMapFunction_01
Verdoppelt Zahlen in verschachtelten Listen und flacht das Ergebnis in eine einfache Liste ab.
```scala
package FlatMapUebungen_05

object FlatMapFunction_01 {
  def main(args: Array[String]): Unit = {
    val verschachtelt = List(List(1, 2), List(3, 4), List(5, 6))
    val verdoppeltUndFlach = verschachtelt.flatMap(sub => sub.map(n => n * 2))
    println(verdoppeltUndFlach)
  }
}
```

### FlatMapFunction_02
Extrahiert alle einzigartigen Lieblingsfarben aus einer Liste von Personen.
```scala
package FlatMapUebungen_05

object FlatMapFunction_02 {
  def main(args: Array[String]): Unit = {
    val personen = List(
      ("Max", List("Blau", "Grün")),
      ("Anna", List("Rot")),
      ("Julia", List("Gelb", "Blau", "Grün"))
    )

    val einzigartigeFarben = personen.flatMap(person => person._2).distinct
    println(einzigartigeFarben)
  }
}
```

## 06 For Comprehensions

For-Comprehensions bieten eine lesbare Syntax für Ketten von `map`, `flatMap` und `filter`.

### ForComprehensionFunction_01
Berechnet die Quadrate der Zahlen von 1 bis 10.
```scala
package ForComprehensionsuebungen_06

object ForComprehensionFunction_01 {
  def main(args: Array[String]): Unit = {
    val numbers = 1 to 10
    val squares = for (n <- numbers) yield n * n
    println(squares)
  }
}
```

### ForComprehensionFunction_02
Filtert alle geraden Zahlen aus dem Bereich von 1 bis 20.
```scala
package ForComprehensionsuebungen_06

object ForComprehensionFunction_02 {
  def main(args: Array[String]): Unit = {
    val numbers = 1 to 20
    val evens = for {
      n <- numbers
      if n % 2 == 0
    } yield n
    println(evens)
  }
}
```

### ForComprehensionFunction_03
Erstellt alle möglichen Kombinationen (Paare) aus einer Liste von Farben und einer Liste von Früchten.
```scala
package ForComprehensionsuebungen_06

object ForComprehensionFunction_03 {
  def main(args: Array[String]): Unit = {
    val colors = List("Red", "Green", "Blue")
    val fruits = List("Apple", "Banana", "Orange")

    val pairs = for {
      c <- colors
      f <- fruits
    } yield (c, f)
    println(pairs)
  }
}
```

### ForComprehensionFunction_04
Ermittelt alle noch nicht abgeschlossenen Aufgaben für eine Liste von Benutzern.
```scala
package ForComprehensionsuebungen_06

object ForComprehensionFunction_04 {
  def main(args: Array[String]): Unit = {
    case class User(name: String, tasks: List[String])

    val users = List(
      User("Alice", List("Task 1", "Task 2", "Task 3")),
      User("Bob", List("Task 1", "Task 4", "Task 5")),
      User("Charlie", List("Task 2", "Task 3", "Task 6"))
    )

    val tasksCompleted = List("Task 1", "Task 3", "Task 5")

    val pendingTasks = for {
      user <- users
      task <- user.tasks
      if !tasksCompleted.contains(task)
    } yield (user.name, task)

    println(pendingTasks)
  }
}
```