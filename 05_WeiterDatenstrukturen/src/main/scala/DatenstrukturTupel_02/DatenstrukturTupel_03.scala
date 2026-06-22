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