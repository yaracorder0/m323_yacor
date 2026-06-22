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