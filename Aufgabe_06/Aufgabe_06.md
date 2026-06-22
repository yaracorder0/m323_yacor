# Aufgabe 06 - Dokumentation

## Vorwissensaktivierung
1. Was heisst in der Informatik IO? <br>
   Klassisch: Input/Output (Eingabe/Ausgabe). In der funktionalen Programmierung ist IO ein Datentyp, der eine Berechnung beschreibt, die einen Seiteneffekt haben kann. Er ist wie ein "Rezept" oder ein Container, der erst ausgeführt wird, wenn wir es explizit sagen.

2. Was ist eine "Pure Function"?<br>
   Eine Funktion, die zwei Bedingungen erfüllt:

- Deterministisch: Gleicher Input führt immer zum exakt gleichen Output.

- Keine Seiteneffekte: Sie verändert nichts ausserhalb ihres eigenen Scopes (keine globalen Variablen ändern, kein Drucken auf der Konsole).

3. Was machen "Impure Functions" damit man sie so nennt?<br>
   Sie interagieren mit der "Aussenwelt". Sie lesen Dateien, generieren Zufallszahlen, greifen auf Datenbanken zu oder verändern den Zustand von Variablen ausserhalb der Funktion.

4. Was ist eine "Lazy Evaluation"?<br>
   "Faule Auswertung". Ein Ausdruck wird nicht sofort berechnet, wenn er definiert wird, sondern erst in dem Moment, in dem sein Wert wirklich benötigt wird.

5. Warum könnte man eine Lazy Evaluation auch als deklarative Programmierung anschauen?<br>
   Weil man beschreibt, was das Ergebnis sein soll (die Logik der Verknüpfung), ohne festzulegen, wann genau die CPU die Rechenarbeit leisten muss. Man definiert Beziehungen zwischen Datenströmen, keine strikten Befehlsabfolgen.

6. Was ist und wie geht eine Rekursion?<br>
   Eine Funktion, die sich selbst aufruft. Damit sie nicht ewig läuft, braucht sie eine Abbruchbedingung (Base Case).


## Übung 01: Course Subscriptions (Listen-Transformationen)

**Beschreibung:**
Diese Übung demonstriert die Arbeit mit Listen und Transformationen in Scala. Es werden Kurse mit Studentenlisten gefiltert, um spezifische Module für bestimmte Personen zu finden. Zudem werden die Kursdaten in eine neue Datenstruktur `CourseSubscriptions_01` transformiert, welche die Anzahl der Einschreibungen pro Kurs enthält.

**Code:**
```scala
package Exercise_01

case class Course(title:String, students:List[String])
case class CourseSubscriptions_01(title: String, totalStudents: Int)

object CourseSubscriptions_01 extends App {
  val courses = List(
    Course("M323", List("Peter", "Petra", "Paul", "Paula")),
    Course("M183", List("Paula", "Franz", "Franziska")),
    Course("M117", List("Paul", "Paula")),
    Course("M114", List("Petra", "Paul", "Paula")),
  )

  // 1. Spezifische Strings für Peter und Petra
  val peterModules = courses
    .filter(_.students.contains("Peter"))
    .map(_.title)
    .mkString(", ")
  println(s"Peter besucht folgende Module: $peterModules")

  val petraModules = courses
    .filter(_.students.contains("Petra"))
    .map(_.title)
    .mkString(", ")
  println(s"Petra besucht folgende Module: $petraModules")

  // 2. Transformation in List[CourseSubscriptions]
  val subscriptions: List[CourseSubscriptions_01] = courses.map { c =>
    CourseSubscriptions_01(c.title, c.students.size)
  }
  println(s"Subscriptions: $subscriptions") 
}
```

---

## Übung 02: IO Monad (Funktionale Seiteneffekte)

**Beschreibung:**
In dieser Aufgabe wird der Umgang mit Seiteneffekten unter Verwendung der `cats-effect` Bibliothek (IO-Monade) gezeigt. Ein Würfelwurf wird als `IO`-Aktion gekapselt, was die Trennung von reiner Logik und unvorhersehbaren Seiteneffekten (Zufallszahlen) ermöglicht. Die `IOApp` dient hierbei als sicherer Einstiegspunkt für die Ausführung der Programme.

**Code:**
```scala
package Exercise_02

import cats.effect.{IO, IOApp}
import cats.effect.unsafe.implicits.global
import scala.util.Random

object IO_01 extends IOApp.Simple {
  def rollDiceImpure(): Int = {
    val random = new Random
    random.nextInt(6) + 1
  }

  // Wrapper: Die Funktion wird hier noch NICHT ausgeführt (Lazy)
  def rollDice(): IO[Int] = IO.delay(rollDiceImpure())

  def allowToLeafHome: IO[Boolean] = {
    val checkRoll = rollDice()
    checkRoll.map(_ == 6) // Pure Logik auf dem IO
  }

  // Die Methode 'run' ist dein neuer Einstiegspunkt (Main-Methode)
  // Du brauchst kein unsafeRunSync mehr, IOApp macht das für dich.
  def run: IO[Unit] = for {
    result <- allowToLeafHome
    _ <- IO.println(s"Darf ich das Haus verlassen? $result")
  } yield ()
}
```

---

## Übung 03: Lazy Lists (Träge Datenstrukturen)

**Beschreibung:**
Diese Übung führt in das Konzept der `LazyList` (träge Listen) ein. Es werden verschiedene unendliche Sequenzen erstellt, darunter einfache Zähler, die 2er-Reihe, 2er-Potenzen und das Alphabet. Träge Listen berechnen ihre Elemente erst dann, wenn sie tatsächlich benötigt werden. Zudem wird gezeigt, wie `LazyList` zusammen mit `IO` verwendet werden kann, um einen kontinuierlichen Stream von Aktionen (z.B. Würfelwürfe) zu definieren.

**Code:**
```scala
package Exercise_03

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import scala.util.Random

object LazyList_01 extends App {
  // 1. Zählt von 1 an weiter
  val counting = LazyList.from(1)
  println(s"1. Count: ${counting.take(5).toList}")

  // 2. 2er-Reihe (2, 4, 6...)
  val evens = LazyList.iterate(2)(_ + 2)
  println(s"2. Evens: ${evens.take(5).toList}")

  // 3. 2er-Potenz (2, 4, 8, 16...)
  val powersOfTwo = LazyList.iterate(2)(_ * 2)
  println(s"3. Powers: ${powersOfTwo.take(5).toList}")

  // 4. Alphabet (a, b, c... aa, ab, ac...)
  def nextCol(s: String): String = {
    val lastIndex = s.length - 1
    if (lastIndex < 0) "a"
    else {
      val lastChar = s.charAt(lastIndex)
      if (lastChar == 'z') {
        nextCol(s.substring(0, lastIndex)) + "a"
      } else {
        s.substring(0, lastIndex) + (lastChar + 1).toChar
      }
    }
  }
  val alphabet = LazyList.iterate("a")(nextCol)
  println(s"4. Alpha: ${alphabet.take(30).toList}")

  // 5. Zufallszahlen 1-6 (LazyList von IOs)
  def rollDice(): IO[Int] = IO.delay(Random.nextInt(6) + 1)

  val randomDiceStream = LazyList.continually(rollDice())
  
  // Um einen Wert zu sehen, kann man Werte entnehmen und ausführen:
  val sampleDice = randomDiceStream.take(5).map(_.unsafeRunSync()).toList
  println(s"5. Zufällige Würfe: $sampleDice")
}
```

---

## Übung 04: Parallele Prozesse und Ref

**Konzept:**
Parallele Prozesse ermöglichen die gleichzeitige Nutzung mehrerer CPU-Kerne, was die Ausführung beschleunigt. Ein zentrales Problem ist der Zugriff auf gemeinsame Daten (Shared State). Scala löst dies elegant mit dem Datentyp `Ref` (aus der Cats Effect Bibliothek).

**Wie funktioniert `Ref.update`?**
Die Methode `Ref.update` erreicht Thread-Sicherheit durch **atomare Operationen** (Compare-and-Swap, CAS). Anstatt die Variable zu sperren (Locking), wird versucht, den Wert zu aktualisieren. Wenn sich der Wert zwischen Lesen und Schreiben geändert hat, wird die Operation automatisch wiederholt, bis sie erfolgreich ist. Dies verhindert "Race Conditions", wie sie im Beispiel mit dem Smartphone-Lagerbestand beschrieben wurden.

**Code:**
```scala
package Exercise_04

import cats.effect.{IO, IOApp, Ref}
import cats.implicits._
import scala.concurrent.duration._
import scala.util.Random

object ParalleleProzesse_01 extends IOApp.Simple {

  def rollDiceImpure(): Int = {
    val random = new Random
    random.nextInt(6) + 1
  }

  def rollDice(): IO[Int] = IO.delay(rollDiceImpure())

  // 1. Code
  val code1: IO[Int] = for {
    _ <- IO.sleep(1.second)
    result <- List(rollDice(), rollDice()).parSequence
  } yield result.sum

  // 2. Code
  val code2: IO[List[Int]] = for {
    storedCasts <- Ref.of[IO, List[Int]](List.empty)
    singleCast = rollDice().flatMap(result => storedCasts.update(_.appended(result)))
    _ <- List(singleCast, singleCast).parSequence
    casts <- storedCasts.get
  } yield casts

  // 3. Code
  val code3: IO[List[Int]] = for {
    storedCasts <- Ref.of[IO, List[Int]](List.empty)
    singleCast = rollDice().flatMap(result => storedCasts.update(_.appended(result)))
    _ <- List.fill(3)(singleCast).parSequence
    casts <- storedCasts.get
  } yield casts

  // 4. Code
  val code4: IO[Int] = for {
    storedCasts <- Ref.of[IO, Int](0)
    singleCast = rollDice().flatMap(result => if (result == 6) storedCasts.update(_ + 1) else IO.unit)
    _ <- List.fill(100)(singleCast).parSequence
    casts <- storedCasts.get
  } yield casts

  // 5. Code
  val code5: IO[Int] = List.fill(100)(IO.sleep(1.second).flatMap(_ => rollDice())).parSequence.map(_.sum)

  def run: IO[Unit] = for {
    _ <- IO.println("--- Start Ausführung der Parallelen Codebeispiele (Aufgabe 4) ---")
    res1 <- code1
    _ <- IO.println(s"1. Code (Summe): $res1")
    res2 <- code2
    _ <- IO.println(s"2. Code (Ref List): $res2")
    res3 <- code3
    _ <- IO.println(s"3. Code (Ref List 3 Würfe): $res3")
    res4 <- code4
    _ <- IO.println(s"4. Code (Anzahl Sechser): $res4 / 100")
    start <- IO.realTime
    res5 <- code5
    end <- IO.realTime
    duration = (end - start).toSeconds
    _ <- IO.println(s"5. Code (Parallelisiert): $res5 (Dauer: ${duration}s)")
    _ <- IO.println("--- Ende der Simulation (Aufgabe 4) ---")
  } yield ()
}
```

### Analyse der Code-Beispiele

**1. Beispiel: Parallele Ausführung und Summierung**
```scala
for {
    _ <- IO.sleep(1.second)
    result <- List(rollDice(), rollDice()).parSequence
} yield result.sum
```
*   **Beschreibung:** Das Programm wartet zunächst eine Sekunde. Danach werden zwei Würfelwürfe (`rollDice`) **parallel** ausgeführt (dank `parSequence`). Zum Schluss wird die Summe der beiden Würfe zurückgegeben.

**2. Beispiel: Shared State mit Ref (2 Würfe)**
```scala
for {
    storedCasts <- Ref.of[IO, List[Int]](List.empty)
    singleCast = rollDice()
    .flatMap(result => storedCasts.update(_.appended(result)))
    _ <- List(singleCast, singleCast).parSequence
    casts <- storedCasts.get
} yield casts
```
*   **Beschreibung:** Hier wird ein `Ref` erstellt, das eine leere Liste speichert. Ein `singleCast` würfelt und fügt das Ergebnis der Liste im `Ref` hinzu. Zwei dieser Aktionen laufen parallel ab. Am Ende wird die Liste mit beiden Ergebnissen aus dem `Ref` ausgelesen.

**3. Beispiel: Shared State mit Ref (3 Würfe)**
```scala
for {
    storedCasts <- Ref.of[IO, List[Int]](List.empty)
    singleCast = rollDice()
    .flatMap(result => storedCasts.update(_.appended(result)))
    _ <- List.fill(3)(singleCast).parSequence
    casts <- storedCasts.get
} yield casts
```
*   **Beschreibung:** Funktioniert identisch wie Beispiel 2, führt jedoch über `List.fill(3)` drei Würfelwürfe parallel aus und speichert alle drei sicher im gemeinsamen `Ref`.

**4. Beispiel: Zähler für Sechser**
```scala
for {
    storedCasts <- Ref.of[IO, Int](0)
    singleCast = rollDice().flatMap(result => if (result == 6) storedCasts.update(_ + 1) else IO.unit)
    _ <- List.fill(100)(singleCast).parSequence
    casts <- storedCasts.get
} yield casts
```
*   **Beschreibung:** Ein `Ref` wird als Zähler (initial 0) verwendet. Es werden 100 Würfelwürfe parallel durchgeführt. Jedes Mal, wenn eine 6 gewürfelt wird, wird der Zähler im `Ref` sicher um 1 erhöht. Das Resultat ist die Gesamtzahl der gewürfelten Sechser.

**5. Beispiel: Performance-Vorteil durch Parallelität**
```scala
List.fill(100)(IO.sleep(1.second).flatMap(_ => rollDice())).parSequence.map(_.sum)
```
*   **Beschreibung:** Es werden 100 Aktionen erstellt, die jeweils 1 Sekunde warten und dann würfeln. Durch `parSequence` laufen diese 100 Sekunden Wartezeit **parallel**. Das gesamte Programm dauert also nur ca. 1 Sekunde (statt 100 Sekunden bei sequenzieller Ausführung) und liefert die Summe aller Würfe zurück.

---

## Zusatzaufgabe: Thread-sichere Lagerverwaltung

**Beschreibung:**
In dieser Zusatzaufgabe wurde eine Lagerverwaltung implementiert, die das zu Beginn beschriebene Problem der Race Conditions löst. Durch die Verwendung von `Ref.modify` wird sichergestellt, dass die Prüfung des Lagerbestands und die anschließende Aktualisierung als eine einzige, atomare Operation ausgeführt werden. Selbst wenn viele Kunden gleichzeitig versuchen, Produkte zu kaufen, bleibt der Lagerbestand jederzeit konsistent.

**Implementierte Funktionen:**
1.  **buyProduct:** Prüft atomar, ob genügend Bestand vorhanden ist, und zieht die Menge ab.
2.  **reorderProducts:** Erhöht den Lagerbestand sicher um eine definierte Menge.
3.  **Simulation:** Parallele Ausführung mehrerer Kauf- und Auffüll-Aktionen mittels `parSequence`.

**Code:**

```scala
package Exercise_05

import cats.effect.{IO, IOApp, Ref}
import cats.implicits._

object Warehouse_01 extends IOApp.Simple {

  case class Warehouse(stock: Ref[IO, Int]) {

    // Thread-sicheres Abbuchen vom Lagerbestand
    def buyProduct(name: String, amount: Int): IO[Unit] = {
      stock.modify { current =>
        if (current >= amount) {
          (current - amount, IO.println(s"$name hat $amount Stück gekauft. Neuer Bestand: ${current - amount}"))
        } else {
          (current, IO.println(s"$name wollte $amount Stück kaufen, aber es sind nur noch $current da!"))
        }
      }.flatten
    }

    // Thread-sicheres Auffüllen des Lagerbestands
    def reorderProducts(amount: Int): IO[Unit] = {
      stock.update(_ + amount) >> IO.println(s"Lager um $amount Stück aufgefüllt.")
    }
  }

  def run: IO[Unit] = {
    for {
      initialStock <- Ref.of[IO, Int](10)
      warehouse = Warehouse(initialStock)

      _ <- IO.println("--- Start Lagerverwaltung Simulation ---")

      buyers = List(
        warehouse.buyProduct("Kunde A", 1),
        warehouse.buyProduct("Kunde B", 2),
        warehouse.buyProduct("Kunde C", 5),
        warehouse.buyProduct("Kunde D", 3),
        warehouse.reorderProducts(5),
        warehouse.buyProduct("Kunde E", 4)
      )

      _ <- buyers.parSequence

      finalStock <- initialStock.get
      _ <- IO.println(s"--- Simulation beendet. Finaler Lagerbestand: $finalStock ---")
    } yield ()
  }
}
```

