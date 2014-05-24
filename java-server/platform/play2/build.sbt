name := "simple"

version := "0"

libraryDependencies ++= Seq(
  "org.atmosphere" % "vibe-runtime" % "3.0.0.Alpha1-SNAPSHOT",
  "org.atmosphere" % "vibe-play2" % "3.0.0.Alpha1-SNAPSHOT"
)

play.Project.playJavaSettings
