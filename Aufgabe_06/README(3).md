# Weitere Algorithmen (Pipelines, IO, Streams, Paralleles)

- [Weitere Algorithmen (Pipelines, IO, Streams, Paralleles)](#weitere-algorithmen-pipelines-io-streams-paralleles)
    - [Datenstrukturen und der Umgang mit "impurity" und parallelen Prozessen\*\*](#datenstrukturen-und-der-umgang-mit-impurity-und-parallelen-prozessen)
  - [Pipelines](#pipelines)
    - [Aufgabe 1:](#aufgabe-1)
  - [IO](#io)
    - [Aufgabe 2](#aufgabe-2)
  - [Streams / LazyList](#streams--lazylist)
    - [Streams vs LazyList in Scala](#streams-vs-lazylist-in-scala)
    - [Aufgabe 3: eigene Lazy Lists schreiben](#aufgabe-3-eigene-lazy-lists-schreiben)
  - [Parallele Prozesse](#parallele-prozesse)
    - [Aufgabe 4 - Code ausführen und verstehen](#aufgabe-4---code-ausführen-und-verstehen)
      - [1. Code](#1-code)
      - [2. Code](#2-code)
      - [3. Code](#3-code)
      - [4. Code](#4-code)
      - [5. Code](#5-code)
    - [Zusatzaufgabe](#zusatzaufgabe)



### Datenstrukturen und der Umgang mit "impurity" und parallelen Prozessen**

In den Kapiteln [04_FunctionsAsValues](../04_FunctionsAsValues) und [05_WeitereDatenstrukturen](../05_WeitereDatenstrukturen) haben 
wir immer wieder Algorithmen beiläufig kennengelernt. Dieses Kapitel beschäftigt sich damit, wie aus kleinen Teilen ein 
grösseres Ganzes wird. Also wie aus den "Grundalgorithmen" oder auch "Grundfunktionen", Programme zusammengesetzt werden 
können. Wir werden aber auch weitere Datenstrukturen kennenlernen die nötig sind, um den "reinen Kern" unserer Applikation 
"rein" zu halten und "unreine" Interaktionen mit anderen Programmen oder dem Nutzer an die "Ränder" unserer Applikation zu 
verschieben, sodass die Businesslogik rein bleibt und wir die Unreinheiten besser unter Kontrolle haben.

Wir werden aber auch sehen, wie einfach parallele Prozesse in der funktionalen Programmierung realisiert werden können - eben 
weil die pure functions nur sehr wenig Abhängigkeiten zur Umgebung besitzen (sind nur abhängig von Input-Parametern und haben 
keinen gemeinsamen Zustand, der synchron gehalten werden müsste).

Wir werden folgende Themen genauer betrachten:
<!-- * **Such-Algorithmen**: TBD. -->
* **Pipelines**: Funktionen werden zur Pipeline indem man sie durch Punkte aneinander gekettet. Im übertragenen Sinne fliessen
die Daten dabei von Funktion zu Funktion. Jede Funktion in der Pipeline erhält den Return-Wert der vorhergehenden Funktion 
und benutzt diese (als Übergabeparameter) zur Weiterverarbeitung.
* **IO**: Ein Datentyp, der die Kommunikation mit Input- und Output-Kanälen repräsentiert und die Möglichkeit bietet den 
Input bzw. Output verzögert ([lazy evaluation](../01_Einführung/Fachbegriffe.md#lazy-evaluation)) auszuwerten. Der 
Programmierer entscheidet im Code, wann die "impure"-Aktion effektiv ausgeführt werden soll... und bis dahin, also 
bis zum Ausführen der "impure"-Aktion, gilt alles als 'pure' was den IO-Datentyp verwendet, 
solange die 'impure'-Aktion noch nicht ausgeführt wird.
* **Streams/LazyList**: Streams sind ein fortlaufender Strom an Daten aus sauberen (pure) oder unsauberen (impure) 
Quellen und sind mit Rekursion realisiert. Streams haben eine Datenquelle (Producer) und eine Datensenke (Consumer) 
und dazwischen weitere Operatoren/Filter, die Kopien der Daten aus der Quelle verändern und weiter reichen bis zum 
eigentlichen Ziel (der Senke) - eine Pipeline zwischen Quelle und Senke. Weil der Streams-Datentyp allerdings den 
Nachteil hat, dass er das erste Element nicht lazy auswertet ([Details](https://stackoverflow.com/questions/60128207/whats-the-difference-between-lazylist-and-stream-in-scala)) 
wurde dieser als deprecated markiert und wird zukünftig durch 'LazyList' ersetzt.
* **Parallele Prozesse**: Parallele Prozesse sind vor allem dann wichtig, wenn mehrere CPU-Kerne für die Abarbeitung 
von Programmen verwendet werden können. Dadurch kann die Verarbeitungszeit insgesamt reduziert werden (weil die 
verwendete CPU-Rechenleistung erhöht wird).

## Pipelines
Pipelines sind aneinander gekettete Funktionen. Der Return-Wert des vorangehenden Funktionsaufrufs kann jeweils im 
darauffolgenden Funktionsaufruf als Parameter verwendet werden.

Gegeben ist folgendes Beispiel:
```
case class Course(title: String, students: List[String])

val courses = List(
    Course("M323", List("Peter", "Petra", "Paul", "Paula")),
    Course("M183", List("Paula", "Franz", "Franziska")),
    Course("M117", List("Paul", "Paula")),
    Course("M114", List("Petra", "Paul", "Paula")),
)

println(courses.map(_.students).filter(_.contains("Peter")).size) // 1
println(courses.map(_.students).filter(_.contains("Petra")).size) // 2
```

Bei der Ausführung der letzten beiden Zeilen geschieht Folgendes:
1. Als Erstes wird die map-Funktion verwendet. Diese kriegt courses vom Typ `List[Course]` als Parameter und als 
Rückgabewert wird eine `List[List[String]]` mit den Teilnehmerlisten aller Lernenden eines Moduls zurückgegeben.
2. Die filter-Funktion kriegt Ihrerseits den Rückgabewert von map als Parameter (vom Typ `List[List[String]]`) 
und gibt nur diejenigen Listen von Lernenden zurück wo entweder "Peter" (zweitletzte Zeile) oder "Petra" (letzte Zeile) 
im Modul eingeschrieben sind. Der Rückgabewert ist wieder vom Typ `List[List[String]]`
3. Als Letztes wird der size-Funktion die gefilterte Liste von Listen (Typ `List[List[String]]`) mit den Teilnehmern 
übergeben. Die size-Funktion zählt ihrerseits nun die Anzahl der Teilnehmerlisten und gibt diesen Wert zurück. 
Dieser entspricht auch der Anzahl (Typ `Int`) in wie vielen Modulen "Peter" respektive "Petra" eingeschrieben sind.

Die gegebene Pipeline transformiert folglich den Typ `List[Course]` in den Typ `Int`. Bei jedem Schritt wird auf einer 
veränderten Kopie der Daten weitergearbeitet.


### Aufgabe 1:

Probieren Sie das Beispiel aus, indem Sie den Code kopieren und ausführen. Verändern Sie die Pipeline so, dass Sie 
anstelle der Anzahl Kurse folgende Strings zurückerhalten:
* Peter besucht folgende Module: M323
* Petra besucht folgende Module: M323, M114

Verändern Sie die Pipeline als Nächstes so, dass Sie eine Liste vom Typ `List[CourseSubscriptions]` zurückgeliefert 
wird. Der Typ `CourseSubscriptions` soll die folgende case class sein: `case class CourseSubscriptions(title: String, totalStudents: Int)`. 
Das Feld `totalStudents` soll dabei die Anzahl der Lernenden enthalten, die im Modul eingeschrieben sind.

Dokumentieren Sie Ihre Lösungen in Ihrem GIT-Repository.

## IO
Die Idee von IO ist, alle Operationen - seien es nun Inputs, Outputs oder andere Calls von impure functions - 
die dafür sorgen würden, dass die eigene pure function impure würde (Funktionen die Seiteneffekte verursachen) so zu 
kapseln, dass die pure functions dadurch nicht impure werden.

Mit IO kann auf einer abstrakten Ebene beschrieben werden (deklarativer Stil), dass mit Daten aus einer unsicheren 
Quelle gearbeitet wird. Mit `IO.delay(/* hier kommt call auf impure function */)` kann die Ausführung des Codes 
verzögert werden. Es wird quasi nur beschrieben, was ausgeführt werden soll, aber noch nicht ausgeführt. Damit bleibt 
die pure function vorerst pure und kann mit dem IO-Typen arbeiten wie mit allen anderen Werten auch.

Wird die Methode unsafeRunSync nun auf den konkreten IO-Daten aufgerufen, dann wird der zuvor in unserer pure function 
definierte unsichere Call der impure function ausgeführt. D. h. Die Ausführung des problematischen Calls kann auf diese 
Weise von der Businesslogik weg an eine Stelle ausgelagert werden, wo zentral alle unsauberen Dinge gemacht werden und/oder
wo mit der Aussenwelt kommuniziert wird.

Das hat die folgenden Vorteile:
* Bündeln der Logik für die Kommunikation zur Aussenwelt
* Business-Logik befreien von Überprüfung der Inhalte und verschiedenen Fälle (sind Daten vorhanden oder nicht, 
wurde Exception geworfen, läuft die Logik allenfalls in Timeouts)
* Der Datentyp IO hinterlässt eine einfach nachzuverfolgende Spur durch die Business-Logik. Dadurch können die 
Teile die von IO-Operationen abhängig sind einfach identifiziert werden.

Ein Beispiel:
```
import scala.util.Random

def rollDiceImpure(): Int = {
    val random = new Random
    random.nextInt(6) + 1
}

// Beispielaufruf
val result = rollDiceImpure()
println("Der Würfel zeigt: $result")
```

Die Funktion `rollDiceImpure` liefert eine Zufallszahl von 1 bis 6. Die Methode ist impure, weil ihr Return-Wert 
nicht nur von den Input-Parametern abhängt, sondern auch von einem Zufallswert.

In der funktionalen Programmierung werden nun diese impure functions gewrappt und so von der Business-Logik getrennt. 
Stellen Sie sich vor, Sie programmieren ein Spiel, indem die Spieler mit Ihren Figuren erst ihre Basis verlassen 
können, wenn ein 6-er gewürfelt wurde. Dafür brauchen Sie die Funktion `allowedToLeafHome`, die aufgrund von einem 
Würfelergebnis entscheidet, ob Sie die Basis verlassen dürfen mit der Spielfigur oder nicht. Würden Sie die 
'impure function' direkt aufrufen, würde ihre neue Funktion ebenfalls impure. Das wollen Sie nicht und hier kommt IO ins Spiel.

Sie können zuerst eine pure function definieren die den Würfelwurf beschreibt:
```
import cats.effect.IO

def rollDice(): IO[Int] = {
    IO.delay(rollDiceImpure())
}
```

Der Trick ist nun, dass die Zeile `IO.delay(rollDiceImpure())` die Funktion (die Impure ist), nicht ausführt, sondern nur zu 
Ausführung vormerkt wird. Die Funktion `rollDice()` gibt dabei nicht das bereits evaluierte Resultat des Würfelwurfes zurück, 
sondern lediglich die Beschreibung, dass ein 'Int' zurückgeliefert werden sollte, sobald der impure Teil ausgeführt wird/würde. 
Es ist also lediglich eine Beschreibung der Dinge die getan werden müssten, ohne dass diese auch tatsächlich ausgeführt werden. 
Dadurch bleibt die Funktion `rollDice()` eine pure Funktion.

Damit haben Sie nun eine pure Funktion die Ihnen das Resultat eines Münzwurfs liefern wird, sobald diese evaluiert wird 
(oder eben eine Exception oder sonstiges). Der IO-Wert wird jedoch erst später an einer Stelle evaluiert, die sowieso 
impure ist (in der Regel das 'main' - Ihr Hauptprogrammfluss). Sie können jetzt also einfach für die Funktion `allowedToLeafHome` 
die Methode `rollDice()` verwenden. Und weil diese 'pure' ist, bleibt auch Ihre `allowedToLeafHome` 'pure'.

In Code könnte die Funktion wie folgt aussehen:
```
def allowToLeafHome: IO[Boolean] = {
    val checkRoll = rollDice()
    checkRoll.map(_ == 6)
}

// call der Funktion... in allowToLeaf ist nun ein IO[Boolean] enthalten
val allowToLeaf = allowToLeafHome
```

Erst wenn mit `unsafeRunSync()` die Evaluation des konkreten Würfelwurfs angestossen wird, wird der Teil der Applikation 
der diesen Anstösst impure (und nur der). Alle bisher definierten Funktionen bleiben weiterhin pure.

Nur der Teil ist impure:
```
import cats.effect.unsafe.implicits.global
allowToLeaf.unsafeRunSync()
```

### Aufgabe 2

Versuchen Sie dieses Beispiel nachzuvollziehen, indem Sie dieses in Ihrer IDE zum Laufen bringen. Sie brauchen dafür 
die cats-Library. Diese können Sie beispielsweise dem Projekt hinzufügen, indem Sie in Ihrer build.sbt folgende Zeile ergänzen:
```
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.4"
```

Weitere Details zur cats-Library: https://typelevel.org/cats-effect/docs/getting-started

<hr>

## Streams / LazyList

Streams können mit Flüssen verglichen werden - ein Fluss von Daten. Flüsse haben ein Anfang (Quelle) und ein Ende 
(Beispielsweise ein See oder das Meer) und einen Teil dazwischen - der eigentliche Fluss... eine Art offene Pipeline, wenn Sie 
so wollen.

Wenn wir dieses Konzept auf die digitale Welt anwenden, sprechen wir von der Quelle als Producer und vom See/Meer als Consumer. 
Die Pipeline bzw. der Fluss dazwischen sind zwischengeschaltete Operationen/Funktionen/Filter (intermediate operations), die 
miteinander verknüpft sind und wo die Daten transformiert werden - im Prinzip eine Pipeline.

Wie im echten Leben können Quellen versiegen. Hier ein Beispiel:
```
val stream1 = Stream(1,2,3)
```

Der stream1 hat nur drei Werte in seiner Quelle. Wenn nun der Consumer die Daten aus dem Producer verwendet / verarbeitet, 
dann kann dieser nur maximal die drei Zahlen erhalten.
```
stream1.toList // produziert List(1,2,3)
stream1.take(5).toList // produziert auch nur List(1,2,3), obwohl aus dem Stream 5 Elemente entnommen werden sollen
```

Streams bieten nun die append- und repeat-Funktion. Mit append können Sie zwei Streams zusammenhängen (das können zwei 
unterschiedliche Streams oder derselbe Stream sein).
```
val stream1 = Stream(1,2,3)
val stream2 = Stream(4,5,6)
val stream3 = stream1.append(stream2)

stream3.toList // produziert List(1,2,3,4,5,6)
```

Wichtig hier: Die append-Funktion ist lazy... d.h. Sie wird erst dann ausgewertet, wenn dies tatsächlich nötig wird. 
Also erst beim Ausdruck `stream3.toList` werden die Streams wirklich aneinander gehängt.

Weil Sie auch Streams "as value" behandeln können, wird es möglich, Streams als Rückgabewert von Funktionen zu haben. 
So können Sie beispielsweise folgenden Pure-Stream (also einen Stream ohne Seiteneffekte) definieren:
```
def numbers(): Stream[Pure, Int] = {
    Stream(1,2,3).append(numbers()) // auf dieser Zeile kommt die Rekursion ins Spiel
}
```

Die Funktion ruft sich selbst auf und liefert einen Stream, der sich *unendlich tief* selbst immer wieder aufruft - 
ohne Abbruchkriterium. 

Weil `append` aber mit [lazy evaluation](../01_Einführung/Fachbegriffe.md#lazy-evaluation) arbeitet, wird die 
unendliche Rekursion nie bis in die Unendlichkeit aufgelöst - es sei denn, Sie wollen vom Stream unendlich viele Daten lesen oder konsumieren.
```
val streamInfinite = numbers()

streamInfinit.take(8).toList // Produziert List(1,2,3,1,2,3,1,2)
```

Mit take(8) werden 8 Elemente dem Stream entnommen. D.h. die numbers-Funktion wird 3 Mal aufgerufen. 
Das Abbruchkriterium dieser unendlichen Rekursion liegt also darin, dass Sie dem Stream immer nur eine 
endliche Menge an Daten entnehmen - halt so viel wie sie gerade brauchen. Würden Sie `streamInfinit.toList` 
schreiben, dann hätte dies ein Endlos-Loop zur Folge. Ihr Programm würde irgendwann bei einem Stackoverflow 
enden, weil die maximale Verschachtelungstiefe für die rekursiven Funktionsaufrufe erreicht ist.

Mit repeat wird ein Stream rekursiv an sich selbst gehängt (und das unendlich häufig). Dank der [lazy evaluation](../01_Einführung/Fachbegriffe.md#lazy-evaluation) 
wird diese Rekursion aber ebenfalls nicht sofort aufgelöst und auch nicht unendlich tief, sondern nur so 
tief wie das tatsächlich nötig ist.

Mit folgendem Code kann dasselbe erreicht werden wie mit der Funktion `numbers()` von oben:
```
val streamInfinite = Stream(1,2,3).repeat

streamInfinit.take(8).toList // Produziert List(1,2,3,1,2,3,1,2)
```

Dabei übernimmt die Funktion `repeat` die ihr übergebene Liste und gibt diese als rekursiven Aufruf an sich selbst zurück. 
(Leider gibt es die Funktion `repeat` nicht mehr.)

Beurteilen Sie dennoch für folgende Code-Stücke, welche Daten produziert werden, um zu überprüfen, ob Sie verstanden haben, 
wie die Streams und die vorgestellten Funktionen zusammenarbeiten:

1. `Stream(1).repeat.take(3).toList`
2. `Stream(1).append(Stream(0,1).repeat).take(4).toList`
3. `Stream(2).map(_ * 13).repeat.take(1).toList`
4. `Stream(13).filter(_ % 2 != 0).repeat.take(2).toList`

Hier ist die Lösung für diese Aufgabe. Wenn Ihre Vorhersagen von den Beispielen 
abweichen, versuchen Sie bitte nachzuvollziehen wieso. Das hilft Ihnen die Thematik richtig zu verstehen. 

```
    Lösung der Mini-Aufgabe:
    1. List(1,1,1)
    2. List(1,0,1,0)
    3. List(26)
    4. List(13,13)
```

### Streams vs LazyList in Scala

Weil Streams als *deprecated* markiert wurden, sollten Sie heutzutage mit **LazyList** arbeiten. LazyList kennt ähnliche Funktionen wie Streams. Beispielsweise `continually`, welches `repeat` bei Streams ersetzen kann oder `concat`, welches dem `append` bei Streams sehr ähnlich ist.

Verwenden Sie nun die API-Dokumentation von [LazyList](https://dotty.epfl.ch/api/scala/collection/immutable/LazyList$.html) 
sowie die [Beschreibung der Klasse](https://dotty.epfl.ch/api/scala/collection/immutable/LazyList.html) an sich 
(inkl. Beispiele) um sich mit LazyList weiter vertraut zu machen. Ihr Ziel dabei ist es, die oben aufgeführten 
Beispiele von Streams (1-4) umzuschreiben, sodass diese mit LazyList funktionieren - also Zeitgemäss sind.

### Aufgabe 3: eigene Lazy Lists schreiben

Mit diesem Wissen sollten Sie nun in der Lage sein, eigene LazyList's zu schreiben die etwas Sinnvolleres tun wie sich 
ständig selbst zu repetieren. Schreiben Sie Streams, die folgende Anforderungen erfüllen:
* Eine LazyList, die von 1 an weiterzählt (also 1, 2, 3, 4, ... usw.)
* Eine LazyList, die die 2er-Reihe generiert (also 2, 4, 6, 8 ... usw.)
* Eine LazyList, die die 2er-Potenz generiert (also 2, 4, 8, 16, 32 ... usw.)
* Eine LazyList, die die Buchstaben der Reihenfolge nach "zählt" (also a, b, c, ... aa, ab, ac ... usw.)
* Schreiben Sie eine LazyList, die Zufallszahlen von 1 bis 6 generiert (Tipp: Arbeiten Sie mit IO)

Dokumentieren Sie Ihre Lösungen in Ihrem GIT-Repository.

<hr>

## Parallele Prozesse
Parallele Prozesse werden dann interessant, wenn eine Applikation auf einem Rechner läuft, der mehrere CPU-Kerne der 
Applikation zur Verfügung stellt. Dadurch können Verarbeitungsschritte parallel durchgeführt werden und schneller 
Resultate geliefert werden. Parallele Prozesse bringen jedoch ein grosses Problem mit sich: gemeinsam verwendete Daten.

Stellen Sie sich ein Online-Shop vor. Im Lager des Shop-Betreibers liegen 10 Stück eines neuen angesagten Smartphones, 
dass jeder haben möchte. Wenn nun viele Benutzer das Smartphone gleichzeitig in den Warenkorb legen möchten, muss die 
Applikation prüfen wie viele Smartphones noch im Lager liegen. Wenn es noch genügend sind, muss das Smartphone in den 
Warenkorb gelegt und der Lagerbestand um eines reduziert werden. 

Um die Problematik zu verdeutlichen, hier nochmals das Beispiel mit zwei Threads:
1. Thread A liest den Lagerbestand (10 Stück)
2. Thread B liest den Lagerbestand (10 Stück)
3. Thread B legt 2 Smartphones in den Warenkorb
4. Thread A legt Smartphone in den Warenkorb
5. Thread B aktualisiert den Lagerbestand auf 8 Stück (10 Stück - 2 Stück)
6. Thread A aktualisiert den Lagerbestand auf 9 Stück (10 Stück - 1 Stück)

Beim 6. Schritt liegt die Ursache für die Problematik. Thread A weiss nicht, dass Thread 2 bereits den Lagerbestand verändert hat. 

Da Thread A sein vorher (bei 1.) gelesene Lagerstand, nicht mehr aktuell ist, rechnet er mit dem falschen Lagerbestand 
und überschreibt die Änderung vom Thread 2 ohne diese mitzuberücksichtigen (der korrekte Lagerbestand wäre 7 Stück).

In Scala gibt es den Datentyp `Ref`, um mit der Problematik umgehen zu können. Der Datentyp bietet mit `Ref.update` eine 
Methode wie man "thread safe" geteilte (shared) Variablen updaten kann. Recherchieren Sie im Internet, wie die Methode dies erreicht.

Um mehrere Threads parallel laufen zu lassen, kennt Scala auf Listen den Zusatz `parSequence` (oder auch `sequence` 
wenn die Dinge hintereinander und nicht parallel laufen sollen). Um bei einem Thread eine gewisse Zeit zu warten, gibt 
es die `sleep`-Funktion.

### Aufgabe 4 - Code ausführen und verstehen

Gegeben sind die folgenden Beispielcodes: 
Führen Sie diese aus und versuchen Sie nachzuvollziehen, was diese tun. Schreiben Sie in eigenen Worten auf, was die 
Funktionen tun. Die verwendete Funktion `rollDice()` kennen Sie ja bereits (verwenden Sie den Code von weiter oben).
Fragen Sie eine KI für eine Beschreibung und vergleichen Sie diese mit Ihrer Lösung. Haben Sie richtig geraten oder die KI? Dokumentieren Sie Ihre Vergleiche.

#### 1. Code
```
for {
    _ <- IO.sleep(1.second)
    result <- List(rollDice(), rollDice()).parSequence
} yield result.sum
```

#### 2. Code
```
for {
    storedCasts <- Ref.of[IO, List[Int]](List.empty)
    singleCast = rollDice()
    .flatMap(result => storedCasts.update(_.appended(result)))
    _ <- List(singleCast, singleCast).parSequence
    casts <- storedCasts.get
} yield casts
```

#### 3. Code
```
for {
    storedCasts <- Ref.of[IO, List[Int]](List.empty)
    singleCast = rollDice()
    .flatMap(result => storedCasts.update(_.appended(result)))
    _ <- List.fill(3)(singleCast).parSequence
    casts <- storedCasts.get
} yield casts
```

#### 4. Code
```
for {
    storedCasts <- Ref.of[IO, Int](0)
    singleCast = rollDice().flatMap(result => if (result == 6) storedCasts.update(_ + 1) else IO.unit)
    _ <- List.fill(100)(singleCast).parSequence
    casts <- storedCasts.get
} yield casts
```

#### 5. Code
```
List.fill(100)(IO.sleep(1.second).flatMap(_ => rollDice())).parSequence.map(_.sum)
```

Dokumentieren Sie die Beschreibungen zu den Codes in Ihrem GIT-Repository

### Zusatzaufgabe
Setzen Sie die zu Beginn beschriebene Lagerverwaltung in eigenem Scala-Code um. Achten Sie dabei darauf, dass Ihre 
Applikation Multithreading-fähig ist (d. h. auf mehrere threads läuft und dennoch den Lagerbestand korrekt nachführt). 
Bei Zeit und Lust können Sie die Lagerverwaltung auch durch eine weitere Funktion `reorderProducts` ergänzen, die 
den Lagerbestand wieder auffüllt. Achten Sie dabei darauf, dass der Lagerbestand "thread safe" aktualisiert wird 
(d.h. dass alles korrekt gerechnet wird und das Eingangs beschriebene Problem bei Ihrer Applikation nicht auftreten kann).

Dokumentieren Sie Ihre Lösungen in Ihrem GIT-Repository.
