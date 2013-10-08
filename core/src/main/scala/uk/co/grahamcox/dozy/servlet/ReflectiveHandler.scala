package uk.co.grahamcox.dozy.servlet

import grizzled.slf4j.Logger
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.lang.reflect.InvocationTargetException
import uk.co.grahamcox.dozy.annotations._
import uk.co.grahamcox.dozy.annotations.{Method => HttpMethod}
import uk.co.grahamcox.dozy.annotations.{Handler => HandlerMethod}

/**
 * Handler that works using Java Reflection to wrap a bean and Method
 */
class ReflectiveHandler(bean: Any, method: Method) extends Handler {
  /** The logger to use */
  private val logger = Logger[this.type]
  
  /** The HTTP Method to use */
  private val httpMethod = getAnnotation[HttpMethod] map { a => a.value } getOrElse("GET")
  /** The HTTP Path to use */
  private val httpPath = new PathMatcher(getAnnotations[Path] map { a => a.value } map { a => a.stripPrefix("/") } mkString("/", "/", ""))

  logger.debug(s"Method: $httpMethod, Path: $httpPath")
  /**
   * Handle the request provided
   * @param req The request to handle
   * @return the response from handling the request
   */
  def handle(req: Request): Response = {
    val args = buildParameters(req)
    try {
      val output = try {
        method.invoke(bean, args : _*)
      }
      catch {
        case e: InvocationTargetException => throw e.getCause()
        case e: Exception => throw e
      }

      logger.debug(s"Method $method responded with $output")
      output match {
        case r: Response => r
        case r: Any => Response(payload = Some(r))
      }
    }
    catch {
      case e: Exception => {
        logger.error("Unhandled exception", e)
        throw e;
      }
    }
  }

  /**
   * Determine if this handler is capable of handling the given request
   * @param req The request to check
   * @return True if we can handle the request. False if not
   */
  def canHandle(req: Request): Boolean = {
    req.getMethod.eq(httpMethod) && httpPath.matchPath(req.getURL).matches
  }

  /**
   * Build the array of parameters needed to execute the wrapped method
   * @param req The request to build the parameters from
   * @return the array of parameters
   */
  private def buildParameters(req: Request): Array[_ <: Object] = Nil.toArray

  /**
   * Helper to get the requested annotation from the method we are working on
   * @param manifest The type of annotation to get
   * @return the annotation
   */
  private def getAnnotationFromMethod[T <: Annotation](implicit manifest: Manifest[T]): Option[T] = {
    Option(method.getAnnotation(manifest.runtimeClass.asInstanceOf[Class[Annotation]])).map { a => a.asInstanceOf[T] }
  }

  /**
   * Helper to get the requested annotation from the class of the method we are working on
   * @param manifest The type of annotation to get
   * @return the annotation
   */
  private def getAnnotationFromClass[T <: Annotation](implicit manifest: Manifest[T]): Option[T] = {
    Option(bean.getClass.getAnnotation(manifest.runtimeClass.asInstanceOf[Class[Annotation]])).map { a => a.asInstanceOf[T] }
  }

  /**
   * Get the annotation of the given type from either the method or the class as needed
   * @param manifest The type of annotation to get
   * @return the annotation
   */
  private def getAnnotation[T <: Annotation](implicit manifest: Manifest[T]): Option[T] = {
    getAnnotationFromMethod[T].orElse(getAnnotationFromClass[T])
  }
  /**
   * Get the annotations of the given type from both the class and the method, in that order
   * @param manifest The type of annotation to get
   * @return the annotations
   */
  private def getAnnotations[T <: Annotation](implicit manifest: Manifest[T]): Traversable[T] = {
    Seq(getAnnotationFromClass[T], getAnnotationFromMethod[T]).flatten
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
