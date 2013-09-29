package uk.co.grahamcox.dozy.spring

import grizzled.slf4j.Logger
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import scala.collection.JavaConverters._
import uk.co.grahamcox.dozy.annotations.Controller
import uk.co.grahamcox.dozy.servlet.Configuration

class SpringConfiguration extends Configuration {
  /** The logger to use */
  private val logger = Logger[this.type]

  /** The property to define the Spring XML Configuration file to use */
  private val SpringConfigurationXml = "dozy.spring.configuration.xml"

  /** The Spring Application Context to use */
  private var applicationContext: Option[ApplicationContext] = None
  /**
   * Provide any configuration that is required for the trait to be able to find the classes needed
   * @param configuration The configuration that is used
   */
  def configure(configuration: Map[String, String]) {
    applicationContext = try {
      configuration.get(SpringConfigurationXml).map({ xml: String =>
        new ClassPathXmlApplicationContext(xml)
      })
    }
    catch {
      case e: Exception => {
        logger.error("Error loading configuration", e)
        None
      }
    }
  }

  /**
   * Get all of the controllers that are defined in this configuration
   * @return the collection of controllers - that is all of the classes with the @Controller annotation
   */
  def controllers: Iterable[AnyRef] = {
    applicationContext.map({ context => 
      context.getBeansWithAnnotation(classOf[Controller]).asScala.values
    }).getOrElse(Nil)
  }
}
