name := "Enron_Email_Analysis"

version := "1.0"

// spark 2.2.1 requires scala 2.11.8
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.2.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.xerial" % "sqlite-jdbc" % "3.21.0.1",
  "com.github.aselab" %% "scala-activerecord" % "0.4.0",
  "mysql" % "mysql-connector-java" % "5.1.+"
)

mainClass in assembly := Some("SparkAnalysisApp")

assemblyMergeStrategy in assembly := {
  case "application.conf" => MergeStrategy.concat
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case _ => MergeStrategy.filterDistinctLines
}
