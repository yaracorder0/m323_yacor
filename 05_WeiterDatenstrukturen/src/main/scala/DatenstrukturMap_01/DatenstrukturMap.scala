package DatenstrukturMap_01

object DatenstrukturMap {
  def main(args: Array[String]): Unit = {
    // Aufgabe 1
    val m1: Map[String, String] = Map("key" -> "value")
    println(m1)

    // Aufgabe 2
    val m2 = m1 + ("key2" -> "value2")
    println(m2)

    // Aufgabe 3
    val m3 = m2 + ("key2" -> "aDifferentValue")
    println(m3)

    // Aufgabe 4
    val m4 = m3 - "key"
    println(m4)

    // Aufgabe 5
    val valueFromM3: Option[String] = m3.get("key")
    println(valueFromM3)

    // Aufgabe 6
    val valueFromM4: Option[String] = m4.get("key")
    println(valueFromM4)
  }
}