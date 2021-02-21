enablePlugins(ScalaJSPlugin)

name := "Bingo"
scalaVersion := "2.13.4"

scalaJSUseMainModuleInitializer := true
mainClass in Compile := Some("bike.mikey.bingo.App")
