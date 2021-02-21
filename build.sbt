enablePlugins(ScalaJSPlugin)

name := "Bingo"
scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.11.0",
  "org.scala-js" %%% "scalajs-dom" % "1.1.0"
)

scalaJSUseMainModuleInitializer := true
mainClass in Compile := Some("bike.mikey.bingo.App")
