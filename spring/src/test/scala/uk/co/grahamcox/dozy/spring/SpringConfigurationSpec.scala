package uk.co.grahamcox.dozy.spring

import grizzled.slf4j.Logger
import org.specs2.mutable._
import uk.co.grahamcox.dozy.annotations._

@Controller
class MyController {
}

class SpringConfigurationSpec extends Specification {
  private val logger = Logger[this.type]
  logger.info("Starting tests")
  "The SpringConfiguration" should {
    "not have any controllers when unconfigured" in {
      val configuration = new SpringConfiguration
      configuration.controllers must be empty
    }

    "not have any controllers when pointing at an invalid configuration" in {
      val configuration = new SpringConfiguration
      configuration.configure(Map("dozy.spring.configuration.xml" -> "classpath:i.dont.exist.xml"))
      configuration.controllers must be empty
    }

    "have the correct controllers when pointing at a valid configuration" in {
      val configuration = new SpringConfiguration
      configuration.configure(Map("dozy.spring.configuration.xml" -> "classpath:uk/co/grahamcox/dozy/spring/springconfigurationspec-context.xml"))
      val controllers = configuration.controllers
      controllers must be size(1)
      controllers.find({ a => a.isInstanceOf[MyController] }) must beSome
    }
  }
}

