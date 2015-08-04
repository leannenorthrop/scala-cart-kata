
// Set the project name to the string 'My Project'
name := "Shopping Cart Kata"

// The := method used in Name and Version is one of two fundamental methods.
// The other method is <<=
// All other initialization methods are implemented in terms of these.
version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits", "-doc-title", "Shopping Cart Kata")

// ScalaDoc guide: https://gist.github.com/VladUreche/8396624