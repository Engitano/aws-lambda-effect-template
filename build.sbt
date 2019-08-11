import sbtassembly.Log4j2MergeStrategy
import sbtrelease.Version

name := "hello-effect"

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.bintrayRepo("engitano", "maven")
)
scalaVersion := "2.12.8"
releaseNextVersion := { ver =>
  Version(ver).map(_.bumpMinor.string).getOrElse("Error")
}
assemblyJarName in assembly := "lambda.jar"

val http4sV = "0.21.0-M2"

libraryDependencies ++= Seq(
  "com.engitano"          %% "aws-effect-lambda-http4s" % "0.1.23",
  "com.engitano"          %% "dynamo-f"                 % "0.1.30",
  "com.amazonaws"         % "aws-lambda-java-log4j2"    % "1.1.0",
  "org.http4s"            %% "http4s-core"              % http4sV,
  "org.http4s"            %% "http4s-dsl"               % http4sV,
  "org.http4s"            %% "http4s-circe"             % http4sV,
  "com.github.pureconfig" %% "pureconfig"               % "0.11.1",
  "org.scanamo"           %% "scanamo"                  % "1.0.0-M10",
  "org.specs2"            %% "specs2-core"              % "4.6.0" % Test,
  "org.mockito"           %% "mockito-scala-specs2"     % "1.5.11" % Test,
  "org.hamcrest"          % "hamcrest-core"             % "1.3" % Test
)

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last == "Log4j2Plugins.dat" =>
    Log4j2MergeStrategy.plugincache
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
