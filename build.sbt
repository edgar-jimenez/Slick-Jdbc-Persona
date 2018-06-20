name := """Slick-Jdbc-Persona"""
organization := "com.ceiba.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)


scalaVersion := "2.11.0"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.41"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "io.reactivex" %% "rxscala" % "0.26.5"
libraryDependencies += "io.reactivex" % "rxjava" % "1.3.4"


routesGenerator := InjectedRoutesGenerator