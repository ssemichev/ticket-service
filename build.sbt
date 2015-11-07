name := "ticket-service"

version := "2.4.0"

scalaVersion := "2.11.7"

lazy val akkaVersion = "2.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.springframework" % "spring-context" % "4.2.2.RELEASE",
  "javax.inject" % "javax.inject" % "1",
  "com.cedarsoftware" % "json-io" % "4.1.9",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test->default"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-a")