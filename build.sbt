enablePlugins(ScalaJSPlugin)

name := "PurpleRain-ScalaJS"
version := "1.0"
scalaVersion := "2.12.2" // or any other Scala version >= 2.10.2

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
