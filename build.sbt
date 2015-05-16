import com.typesafe.sbt.SbtAspectj._
import sbtassembly.MergeStrategy

name := "albgorski-akka-kamon"

organization := "albgorski.akka.kamon"

version := "1.0.0"

scalaVersion := "2.11.6"



val kamonVersion = "0.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.10",
  "io.kamon" %% "kamon-akka" % kamonVersion,
  "io.kamon" %% "kamon-statsd" % kamonVersion,
  "io.kamon" %% "kamon-log-reporter" % kamonVersion,
  "io.kamon" %% "kamon-system-metrics" % kamonVersion,
  "org.aspectj" % "aspectjweaver" % "1.8.4" % "provided"
)

javaOptions in run ++= Seq(
  "-Djava.library.path=./sigar",
  "-Xms128m", "-Xmx1024m")


aspectjSettings

javaOptions <++= AspectjKeys.weaverOptions in Aspectj in run

// Assembly settings
mainClass in Global := Some("albgorski.akka.kamon.Main")

assemblyJarName in assembly := s"${name.value}.jar"

// sbt-assembly merge strategy for aop.xml files by @colestanfield
// https://gist.github.com/colestanfield/fac042d3108b0c06e952
val aopMerge: MergeStrategy = new MergeStrategy {
  val name = "aopMerge"

  import scala.xml._
  import scala.xml.dtd._

  def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
    val dt = DocType("aspectj", PublicID("-//AspectJ//DTD//EN", "http://www.eclipse.org/aspectj/dtd/aspectj.dtd"), Nil)
    val file = MergeStrategy.createMergeTarget(tempDir, path)
    val xmls: Seq[Elem] = files.map(XML.loadFile)
    val aspectsChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "aspects" \ "_")
    val weaverChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "weaver" \ "_")
    val options: String = xmls.map(x => (x \\ "aspectj" \ "weaver" \ "@options").text).mkString(" ").trim
    val weaverAttr = if (options.isEmpty) Null else new UnprefixedAttribute("options", options, Null)
    val aspects = new Elem(null, "aspects", Null, TopScope, false, aspectsChildren: _*)
    val weaver = new Elem(null, "weaver", weaverAttr, TopScope, false, weaverChildren: _*)
    val aspectj = new Elem(null, "aspectj", Null, TopScope, false, aspects, weaver)
    XML.save(file.toString, aspectj, "UTF-8", xmlDecl = false, dt)
    IO.append(file, IO.Newline.getBytes(IO.defaultCharset))
    Right(Seq(file -> path))
  }
}

// Use defaultMergeStrategy with a case for aop.xml
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "aop.xml") =>
    aopMerge
  case x =>
    (assemblyMergeStrategy in assembly).value(x)
}

fork in run := true