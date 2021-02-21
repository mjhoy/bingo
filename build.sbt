enablePlugins(ScalaJSPlugin)

name := "Bingo"
scalaVersion := "2.13.4"
semanticdbEnabled := true
semanticdbVersion := scalafixSemanticdb.revision

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.11.0",
  "org.scala-js" %%% "scalajs-dom" % "1.1.0",
)

ThisBuild / scalafixDependencies ++= Seq(
  "com.github.liancheng" %% "organize-imports" % "0.5.0",
)

lazy val compilerOptions = Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Wconf:cat=unused:info",
  "-Wunused",
  "-Xfatal-warnings",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Yrangepos",
)

lazy val nonConsoleOptions = Seq(
  "-Wunused",
  "-Ywarn-unused",
  "-Xfatal-warnings",
)

scalacOptions ++= compilerOptions
Compile / console / scalacOptions ~= (_ filterNot (nonConsoleOptions.contains(_)))

scalaJSUseMainModuleInitializer := true
mainClass in Compile := Some("bike.mikey.bingo.App")

addCommandAlias(
  "checkAll",
  "scalafmtCheck ; scalafix --check",
)

addCommandAlias(
  "fixAll",
  "scalafmt ; scalafix",
)
