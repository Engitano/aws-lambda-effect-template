package lambda

import cats.effect.IO
import cats.instances.either._
import cats.syntax.bifunctor._
import com.engitano.awseffect.lambda.http4s.LambdaHost
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext

object Application extends Http4sDsl[IO] {
  implicit val ec = ExecutionContext.global
  implicit val cs = IO.contextShift(ec)

  case class ApplicationConfig(version: String)
  case class Environment(appConfig: ApplicationConfig)

  case class HealthCheckResponse(version: String, status: Option[String])

  val environment = IO.fromEither(pureconfig.loadConfig[Environment]
    .leftMap(e => new Exception(e.tail.foldLeft(e.head.description)(_ + _.description))))

  def routes(env: Environment) = HttpRoutes.of[IO] {
    case GET -> Root / "healthcheck" => Ok(HealthCheckResponse(env.appConfig.version, None).asJson)
  }

  class Handler extends LambdaHost[IO](environment.map(routes))
}
