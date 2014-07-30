name := "simple"

version := "0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.atmosphere" % "vibe" % "3.0.0-Alpha1-SNAPSHOT",
  "org.atmosphere" % "vibe-platform-play2" % "3.0.0-Alpha1-SNAPSHOT"
)

resolvers += (
    "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
)