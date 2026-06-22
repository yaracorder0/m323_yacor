ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.8.4"

lazy val root = (project in file("."))
  .settings(
    name := "Aufgabe_06",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.4"
  )
