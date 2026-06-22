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
  println(s"4. Alpha (erste 30 Elemente): ${alphabet.take(30).toList}")

  // 5. Zufallszahlen 1-6 (LazyList von IOs)
  def rollDice(): IO[Int] = IO.delay(Random.nextInt(6) + 1)

  val randomDiceStream = LazyList.continually(rollDice())
  
  // Um einen Wert zu sehen, kann man Werte entnehmen und ausführen:
  val sampleDice = randomDiceStream.take(5).map(_.unsafeRunSync()).toList
  println(s"5. Zufällige Würfe: $sampleDice")
}