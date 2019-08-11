package lambda

import com.engitano.awseffect.lambda.apigw.{ProxyRequest, RequestContext}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import io.circe.parser.decode
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import com.amazonaws.services.lambda.runtime.Context
import com.engitano.awseffect.lambda.apigw.ProxyResponse
import lambda.Application.HealthCheckResponse

class ApplicationSpec extends org.specs2.mutable.Specification with org.mockito.specs2.Mockito {
  "Healthcheck Lambda Spec" >> {
    val handler = new Application.Handler()
    "Must return the configured version" >> {
      val dummyReq = ProxyRequest("", "/healthcheck", "GET", RequestContext("", "", "", "", "", "", ""))
      val reqJson  = dummyReq.asJson.toString.getBytes()
      val input    = new ByteArrayInputStream(reqJson)
      val output   = new ByteArrayOutputStream()
      handler.handleRequest(input, output, mock[Context])

      val resJson   = new String(output.toByteArray())
      val responseE = decode[ProxyResponse](resJson)
      responseE must beRight
      responseE match {
        case Right(r) =>
          r.body match {
            case Some(b) =>
              decode[HealthCheckResponse](b) match {
                case Right(hcr) => hcr.version must beEqualTo("0.1")
                case Left(_)    => ko("Invalid HealthCheckResponse")
              }
            case None => ko("Expected a body")
          }
        case _ => ko("Invalid proxy response")
      }
    }
  }
}
