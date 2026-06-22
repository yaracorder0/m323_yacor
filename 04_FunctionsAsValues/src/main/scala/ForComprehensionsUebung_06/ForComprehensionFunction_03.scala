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