import sbtrelease.Version

name := "hello-effect"

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.bintrayRepo("engitano", "maven")
)
scalaVersion := "2.13.0"
releaseNextVersion := { ver =>
  Version(ver).map(_.bumpMinor.string).getOrElse("Error")
}

val http4sV = "0.21.0-M2"

libraryDependencies ++= Seq(
  "com.engitano"          %% "aws-effect-lambda-http4s" % "0.1.26",
  "com.engitano"          %% "dynamo-f"                 % "0.1.38",
  "com.amazonaws"         % "aws-lambda-java-log4j2"    % "1.1.0",
  "org.http4s"            %% "http4s-core"              % http4sV,
  "org.http4s"            %% "http4s-dsl"               % http4sV,
  "org.http4s"            %% "http4s-circe"             % http4sV,
  "io.circe"              %% "circe-refined"            % "0.12.0-RC2",
  "com.github.pureconfig" %% "pureconfig"               % "0.11.1",
  "org.specs2"            %% "specs2-core"              % "4.6.0" % Test,
  "org.mockito"           %% "mockito-scala-specs2"     % "1.5.11" % Test,
  "org.hamcrest"          % "hamcrest-core"             % "1.3" % Test
)

Assembly.settings
