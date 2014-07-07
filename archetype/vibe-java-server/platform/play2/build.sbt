name := "simple"

version := "0"

libraryDependencies ++= Seq(
  "org.atmosphere" % "vibe" % "3.0.0-Alpha1-SNAPSHOT",
  "org.atmosphere" % "vibe-platform-play2" % "3.0.0-Alpha1-SNAPSHOT"
)

play.Project.playJavaSettings
