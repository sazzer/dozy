package uk.co.grahamcox.dozy.annotations

import org.specs2.mutable._

class ControllerSpec extends Specification {
  "The 'Controller' Annotation" should {
    @Controller
    class MyController()

    "be available on the class at runtime" in {
      val controller = new MyController()

      val annotation = Option(controller.getClass.getAnnotation(classOf[Controller]))
      annotation must beSome
    }
  }
}
