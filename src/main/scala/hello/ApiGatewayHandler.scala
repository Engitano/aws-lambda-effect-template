package hello

import cats.effect.IO
import com.amazonaws.services.lambda.runtime.Context
import com.engitano.awseffect.lambda.apigw.{ApiGatewayLambda, Dsl, ProxyRequest, ProxyResponse}
import com.engitano.awseffect.marshalling.circe._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext

object ApiGatewayHandler {
  implicit val ec = ExecutionContext.global
  implicit val cs = IO.contextShift(ec)
  implicit val ce = IO.ioConcurrentEffect
  case class TestResponse(status: String, message: Option[String])
}

import hello.ApiGatewayHandler._

class ApiGatewayHandler extends ApiGatewayLambda[IO] with Dsl[IO] {

  override protected def handle(i: ProxyRequest, c: Context): IO[ProxyResponse] = {
    val msg = i.queryStringParameters
      .flatMap(_.get("msg"))
      .map(msg => s"Hello, $msg")
    ok(TestResponse("OK", msg))
  }
}
