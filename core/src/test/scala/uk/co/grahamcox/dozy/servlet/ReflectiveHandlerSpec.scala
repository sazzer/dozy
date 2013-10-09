package uk.co.grahamcox.dozy.servlet

import javax.servlet.ServletConfig
import org.specs2.mock._
import org.specs2.mutable._
import scala.collection.JavaConverters._
import uk.co.grahamcox.dozy.annotations._
import uk.co.grahamcox.dozy.annotations.{Handler => HandlerMethod}

class ReflectiveHandlerSpec extends Specification with Mockito {
  "The handler" should {
    "when executing a no-args method" in {
      abstract class Handler {
        def handle(): Any
      }
      val method = classOf[Handler].getMethod("handle")
      val request = mock[Request]

      "that returns a Response object" in {
        val handler = mock[Handler]
        val reflectiveHandler = new ReflectiveHandler(handler, method)
        handler.handle() returns Response(300)
        val response = reflectiveHandler.handle(request)
        response must beAnInstanceOf[Response]
        response.statusCode must beEqualTo(300)
        response.payload must beNone
        there was one(handler).handle()
      }

      "that returns a String object" in {
        val handler = mock[Handler]
        val reflectiveHandler = new ReflectiveHandler(handler, method)
        handler.handle() returns "Hello, World"
        val response = reflectiveHandler.handle(request)
        response must beAnInstanceOf[Response]
        response.statusCode must beEqualTo(200)
        response.payload must beSome("Hello, World")
        there was one(handler).handle()
      }

      "that throws" in {
        val handler = mock[Handler]
        val reflectiveHandler = new ReflectiveHandler(handler, method)
        handler.handle() throws new NullPointerException
        reflectiveHandler.handle(request) must throwA[NullPointerException]
        there was one(handler).handle()
      }
    }
    "when executing an unannotated args method" in {
      abstract class Handler {
        def handle(a: String): Any
      }
      val method = classOf[Handler].getMethod("handle", classOf[String])
      val request = mock[Request]

      "that returns a Response object" in {
        val handler = mock[Handler]
        val reflectiveHandler = new ReflectiveHandler(handler, method)
        handler.handle(null) returns Response(300)
        val response = reflectiveHandler.handle(request)
        response must beAnInstanceOf[Response]
        response.statusCode must beEqualTo(300)
        response.payload must beNone
        there was one(handler).handle(null)
      }
    }
  }

  "The builder" should {
    "find no methods on String" in {
      val handlers = ReflectiveHandler.build("hello")
      handlers must be empty
    }
    "find handlers annotated with @Path" in {
      class MyController {
        @Path("/a")
        def method() {

        }
      }
      val handlers = ReflectiveHandler.build(new MyController)
      handlers must have size(1)
    }
    "find handlers annotated with @Handler" in {
      class MyController {
        @HandlerMethod
        def method() {

        }
      }
      val handlers = ReflectiveHandler.build(new MyController)
      handlers must have size(1)
    }
  }
}



