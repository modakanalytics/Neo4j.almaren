ThisBuild / name := "neo4j.almaren"
ThisBuild / organization := "com.github.music-of-the-ainur"

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.10"

crossScalaVersions := Seq(scala211,scala212)
ThisBuild / scalaVersion := scala211

val sparkVersion = "2.4.5"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "com.github.music-of-the-ainur" %% "almaren-framework" % "0.9.0-2.4" % "provided",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.neo4j" %% "neo4j-connector-apache-spark" % "4.0.2_for_spark_2.4"
)

enablePlugins(GitVersioning)

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/service/local/repositories/releases/content"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/modakanalytics/neo4j.almaren"),
    "scm:git@github.com:modakanalytics/neo4j.almaren.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "mantovani",
    name  = "Daniel Mantovani",
    email = "daniel.mantovani@modak.com",
    url   = url("https://github.com/music-of-the-ainur")
  ),
  Developer(
    id    = "sridhar-sid",
    name  = "Sridhar Mudiraj",
    email = "sridhar.mudiraj@modak.com",
    url   = url("https://github.com/music-of-the-ainur")
  ),
    Developer(
    id    = "kiranreddy-modak",
    name  = "Kiran Bolla",
    email = "kiran.bolla@modak.com",
    url   = url("https://github.com/music-of-the-ainur")
  ),
  Developer(
    id    = "praveenkumarb1207",
    name  = "Praveen Kumar",
    email = "praveen.bachu@modak.com",
    url   = url("https://github.com/music-of-the-ainur")
  )
)

ThisBuild / description := "Neo4j Connector For Almaren Framework"
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage := Some(url("https://github.com/music-of-the-ainur/neo4j.almaren"))
ThisBuild / organizationName := "Modak Analytics"
ThisBuild / organizationHomepage := Some(url("https://github.com/modakanalytics"))


// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true
