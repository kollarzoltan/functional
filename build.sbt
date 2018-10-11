name := "Functional"

organization := "hu.zoltankollar"

version := "1.0"

scalaVersion := "2.12.4"

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-feature",
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.scalamock" %% "scalamock" % "4.1.0" % Test,
)
