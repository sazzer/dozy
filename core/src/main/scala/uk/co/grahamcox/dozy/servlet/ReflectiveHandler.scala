package uk.co.grahamcox.dozy.servlet

import grizzled.slf4j.Logger
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import uk.co.grahamcox.dozy.annotations._
import uk.co.grahamcox.dozy.annotations.{Handler => HandlerMethod}

/**
 * Handler that works using Java Reflection to wrap a bean and Method
 */
class ReflectiveHandler(bean: Any, method: Method) extends Handler {
  /**
   * Handle the request provided
   * @param req The request to handle
   * @return the response from handling the request
   */
  def handle(req: Request): Response = {
    Response(200)
  }

  /**
   * Determine if this handler is capable of handling the given request
   * @param req The request to check
   * @return True if we can handle the request. False if not
   */
  def canHandle(req: Request): Boolean = {
    false
  }
}

/**
 * Companion Object for the ReflectiveHandler class that will interrogate classes
 * and produce a collection of Handler objects for them based on the class definition
 */
object ReflectiveHandler {
  /** The logger to use */
  private val logger = Logger[this.type]
  /**
   * Build all of the handlers defined on the provided bean
   * @param bean The bean to build the handlers for
   * @return the handlers
   */
  def build(bean: Any): Traversable[Handler] = {
    bean.getClass.getMethods filter {
      m: Method => {
        m.getDeclaredAnnotations.exists {
          a => {
            logger.trace(s"Checking annotation $a on method $m")
            a match {
              case _:HandlerMethod => {
                logger.trace("Found @Handler")
                true
              }
              case _:Annotation if a.annotationType.isAnnotationPresent(classOf[HandlerMethod]) => {
                logger.trace(s"Found annotation $a with @Handler on it")
                true
              }
              case _ => {
                logger.trace(s"Annotation $a is neither @Handler nor one that has @Handler on it")
                false
              }
            }
          }
        }
      }
    } map {
      m: Method => {
        logger.debug(s"Building handler for method $m")
        new ReflectiveHandler(bean, m)
      }
    }
  }
}
