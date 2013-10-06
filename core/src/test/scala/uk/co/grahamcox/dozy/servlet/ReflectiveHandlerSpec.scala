package uk.co.grahamcox.dozy.servlet

import javax.servlet.ServletConfig
import org.specs2.mock._
import org.specs2.mutable._
import scala.collection.JavaConverters._
import uk.co.grahamcox.dozy.annotations._
import uk.co.grahamcox.dozy.annotations.{Handler => HandlerMethod}

class ReflectiveHandlerSpec extends Specification with Mockito {
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



