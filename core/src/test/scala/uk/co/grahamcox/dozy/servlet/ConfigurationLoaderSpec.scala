package uk.co.grahamcox.dozy.servlet

import javax.servlet.ServletConfig
import org.specs2.mock._
import org.specs2.mutable._
import scala.collection.JavaConverters._

class TestConfiguration extends Configuration {
  /**
   * Provide any configuration that is required for the trait to be able to find the classes needed
   * @param configuration The configuration that is used
   */
  def configure(configuration: Map[String, String]) {
  }
  /**
   * Get all of the controllers that are defined in this configuration
   * @return the collection of controllers - that is all of the classes with the @Controller annotation
   */
  def controllers: Iterable[AnyRef] = Nil
}

class ConfigurationLoaderSpec extends Specification with Mockito {
  "The Configuration Loader Annotation" should {
    "return None when no class specified" in {
      val servletConfig = mock[ServletConfig]
      val initParameters: Map[String, String] = Map.empty[String, String]
      servletConfig.getInitParameterNames().asInstanceOf[java.util.Enumeration[String]] returns initParameters.keys.toIterator.asJavaEnumeration
      initParameters.foreach {
        case (key, value) => servletConfig.getInitParameter(key) returns value
      }

      val configuration = ConfigurationLoader.loadConfiguration(servletConfig)
      configuration must beNone
    }
    "return None when a non-existant class is specified" in {
      val servletConfig = mock[ServletConfig]
      val initParameters: Map[String, String] = Map("dozy.configuration.class" -> "uk.co.grahamcox.dozy.servlet.NonExistantClass")
      servletConfig.getInitParameterNames().asInstanceOf[java.util.Enumeration[String]] returns initParameters.keys.toIterator.asJavaEnumeration
      initParameters.foreach {
        case (key, value) => servletConfig.getInitParameter(key) returns value
      }

      val configuration = ConfigurationLoader.loadConfiguration(servletConfig)
      configuration must beNone
    }
    "return None when a class that is not a Configuration is specified" in {
      val servletConfig = mock[ServletConfig]
      val initParameters: Map[String, String] = Map("dozy.configuration.class" -> "java.lang.String")
      servletConfig.getInitParameterNames().asInstanceOf[java.util.Enumeration[String]] returns initParameters.keys.toIterator.asJavaEnumeration
      initParameters.foreach {
        case (key, value) => servletConfig.getInitParameter(key) returns value
      }

      val configuration = ConfigurationLoader.loadConfiguration(servletConfig)
      configuration must beNone
    }
    "return the correct value when a valid Class is specified" in {
      val servletConfig = mock[ServletConfig]
      val initParameters: Map[String, String] = Map("dozy.configuration.class" -> "uk.co.grahamcox.dozy.servlet.TestConfiguration")
      servletConfig.getInitParameterNames().asInstanceOf[java.util.Enumeration[String]] returns initParameters.keys.toIterator.asJavaEnumeration
      initParameters.foreach {
        case (key, value) => servletConfig.getInitParameter(key) returns value
      }

      val configuration = ConfigurationLoader.loadConfiguration(servletConfig)
      configuration must beSome
    }
  }
}


