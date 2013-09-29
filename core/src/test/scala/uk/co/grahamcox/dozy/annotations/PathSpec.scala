package uk.co.grahamcox.dozy.annotations

import org.specs2.mutable._

class PathSpec extends Specification {
  "The 'Path' Annotation" should {
    @Path("/api")
    class MyController() {
      @Path("/debug")
      def debugHandler = 1
    }

    val controller = new MyController()

    "be available on the class at runtime" in {
      val annotation = Option(controller.getClass.getAnnotation(classOf[Path]))
      annotation must beSome
    }
    
    "retain it's value on the class at runtime" in {
      val annotation = Option(controller.getClass.getAnnotation(classOf[Path]))
      annotation must beSome.which( path => {
        path.value.equals("/api")
        }
      )
    }
    
    "be available on the method at runtime" in {
      val method = controller.getClass.getMethod("debugHandler")
      val annotation = Option(method.getAnnotation(classOf[Path]))
      annotation must beSome
    }
    
    "retain it's value on the method at runtime" in {
      val method = controller.getClass.getMethod("debugHandler")
      val annotation = Option(method.getAnnotation(classOf[Path]))
      annotation must beSome.which( path => {
        path.value.equals("/debug")
        }
      )
    }
  }
}

