package uk.co.grahamcox.dozy.servlet

import grizzled.slf4j.Logger
import javax.servlet.ServletConfig
import scala.collection.JavaConverters._

/**
 * The mechanism by which to load the configuration object
 */
object ConfigurationLoader {
  /** The logger to use */
  private val logger = Logger[this.type]
  /** The Init Parameter that defines the Configuration Class to use */
  private val ConfigurationClassName = "dozy.configuration.class"
  /**
   * Try to determine what Configuration to load, and load it, based on the properties defined in the
   * provided Servlet Config object
   * @param servletConfig the servlet config object to load from
   * @return the loaded Configuration, or None if one couldn't be loaded
   */
  def loadConfiguration(servletConfig: ServletConfig): Option[Configuration] = {
    val initParams = servletConfig.getInitParameterNames.asScala.map({ name => 
      name.asInstanceOf[String]
    }).map({ name: String =>
      (name -> servletConfig.getInitParameter(name))
    }).toMap

    val configClass: Option[Class[_]] = initParams.get(ConfigurationClassName).flatMap({ className: String =>
      logger.debug("Attempting to load configuration class: " + className)
      try {
        Some(Class.forName(className))
      }
      catch {
        case e : ClassNotFoundException => {
          logger.warn("Failed to load class:" + className, e)
          None
        }
      }
    })
    
    (configClass match {
      case Some(configClass: Class[_]) if classOf[Configuration].isAssignableFrom(configClass) => {
        logger.debug("Loaded class: " + configClass)
        Some(configClass.newInstance.asInstanceOf[Configuration])
      }
      case Some(configClass) => {
        logger.warn("Class not of correct type: " + configClass)
        None
      }
      case None => {
        logger.warn("No class loaded")
        None
      }
    }).map({ configuration: Configuration =>
      configuration.configure(initParams)
      configuration
    })
  }
}
