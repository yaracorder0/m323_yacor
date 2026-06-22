package Exercise_04

import cats.effect.{IO, IOApp, Ref}
import cats.implicits._
import scala.concurrent.duration._
import scala.util.Random

object ParalleleProzesse_01 extends IOApp.Simple {

  // Hilfsmethode wie im Dokument beschrieben
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

  // 5. Code (dauert dank parSequence nur ca 1. Sekunde statt 100 Sekunden)
  val code5: IO[Int] = List.fill(100)(IO.sleep(1.second).flatMap(_ => rollDice())).parSequence.map(_.sum)

  def run: IO[Unit] = for {
    _ <- IO.println("--- Start Ausführung der Parallelen Codebeispiele (Aufgabe 4) ---")

    res1 <- code1
    _ <- IO.println(s"1. Code (Summe von 2 parallelen Würfen nach 1s Schlaf): $res1")

    res2 <- code2
    _ <- IO.println(s"2. Code (2 Würfe parallel in Ref[List[Int]] gespeichert): $res2")

    res3 <- code3
    _ <- IO.println(s"3. Code (3 Würfe parallel in Ref[List[Int]] gespeichert): $res3")

    res4 <- code4
    _ <- IO.println(s"4. Code (Anzahl Sechser bei 100 parallelen Würfen): $res4 / 100")

    // Zeitmessung für Code 5, um den Geschwindigkeitsvorteil der Parallelität zu zeigen:
    start <- IO.realTime
    res5 <- code5
    end <- IO.realTime
    duration = (end - start).toSeconds
    _ <- IO.println(s"5. Code (Summe von 100 parallel verzögerten Würfen): $res5 (Dauer: ${duration}s - parallelisiert)")
    
    _ <- IO.println("--- Ende der Simulation (Aufgabe 4) ---")
  } yield ()
}
