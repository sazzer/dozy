package uk.co.grahamcox.dozy.annotations

import org.specs2.mutable._
import scala.reflect.runtime.universe

@Controller case class MyController()
class ControllerSpec extends Specification {
  "The 'Controller' annotation" should {

    val controller = MyController()
    val mirror = universe.runtimeMirror(controller.getClass.getClassLoader)
    val symbol = mirror.reflect(controller).symbol
    val annotations = symbol.annotations

    "be visible at runtime" in {
      annotations.find(a => a.tpe == universe.typeOf[Controller]) must beSome
    }
  }
}
