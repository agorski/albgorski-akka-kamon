import com.typesafe.sbt.SbtAspectj._

name := """albgorski-akka-kamon"""

version := "2.3.5"

scalaVersion := "2.11.4"

val kamonVersion = "0.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.10",
  "io.kamon" %% "kamon-core" % kamonVersion,
  "io.kamon" %% "kamon-scala" % kamonVersion,
  "io.kamon" %% "kamon-akka" % kamonVersion,
  "io.kamon" %% "kamon-statsd" % kamonVersion,
  "io.kamon" %% "kamon-log-reporter" % kamonVersion,
  "io.kamon" %% "kamon-system-metrics" % kamonVersion,
  "org.aspectj" % "aspectjweaver" % "1.8.5"
)

javaOptions in run ++= Seq(
  "-Djava.library.path=./sigar",
  "-Xms128m", "-Xmx1024m")


aspectjSettings

javaOptions <++= AspectjKeys.weaverOptions in Aspectj

fork in run := true