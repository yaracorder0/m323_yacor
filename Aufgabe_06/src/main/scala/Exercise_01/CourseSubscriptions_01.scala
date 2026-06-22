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