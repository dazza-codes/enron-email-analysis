name := "Enron Emails"

version := "1.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-file" % "0.16",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.18",
  "com.typesafe.akka" %% "akka-actor" % "2.5.9",
  "com.typesafe.akka" %% "akka-stream" % "2.5.9",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.9" % Test
)

