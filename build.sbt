name := "My Project"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

//libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"

scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits", "-doc-title", "Shopping Cart Kata")

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 95

ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := false

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := {
    if(scalaBinaryVersion.value == "2.11") true
    else false
}

mainClass in (Compile, run) := Some("org.northrop.leanne.shoppingcart.Main")