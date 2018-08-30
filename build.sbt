name := "lambdale"

version := "1.0"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.0-RC1",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "joda-time" % "joda-time" % "2.9.7"
)

scalacOptions ++= Seq(
  "-language:higherKinds"
)