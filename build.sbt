name := """med_scala"""
organization := "com.viral"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.3"

libraryDependencies ++= Seq(
  guice,
  "org.playframework" %% "play-slick" % "6.1.1",
  "org.playframework" %% "play-slick-evolutions" % "6.1.1",
  "org.playframework" %% "play-json" % "3.0.4",
  "com.h2database" % "h2" % "2.3.232",
  "org.postgresql" % "postgresql" % "42.7.4",
  "com.typesafe.play" %% "play-jdbc-api" % "2.9.5",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.viral.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.viral.binders._"
