package uk.co.grahamcox.dozy.servlet

import grizzled.slf4j.Logger
import scala.util.matching.Regex

/**
 * Base class to represent the result of matching a path
 */
sealed abstract class PathMatch {
  /**
   * Determine whether this was a match or not
   * @return True if this was a match. False if not
   */
  def matches: Boolean
}

/**
 * Implementation of the PathMatch to represent when the path actually did match
 */
final case class PathMatched(parts: Map[String, String]) extends PathMatch {
  /**
   * Determine whether this was a match or not
   * @return True
   */
  def matches: Boolean = true
}

/**
 * Implementation of the PathMatch to represent when the path didn't match
 */
final case class PathUnmatched() extends PathMatch {
  /**
   * Determine whether this was a match or not
   * @return False
   */
  def matches: Boolean = false
}
/**
 * Helper to try and match an actual path to a template
 * @param pathSpec the specification of the path to try and match
 */
class PathMatcher(pathSpec: String) {
  /** The logger to use */
  private val logger = Logger[this.type]
  
  /** The array of names for the extracted parts */
  private val partNames = (pathSpec.stripPrefix("/").split("/").filter { 
    a => a.startsWith(":") 
  } map { 
    a => a.stripPrefix(":") 
  }).zipWithIndex.toList.map {
    case (name, index) => (name, index + 1)
  }
  /** The regex to match against */
  private val regex = new Regex(pathSpec.stripPrefix("/").split("/").map {
    part => part match {
      case name: String if name.startsWith(":") => "([^/]*)"
      case static: String => static
    }
  } mkString("^/", "/", "$"))
  /**
   * Attempt to match the provided path with the path specification
   * @param path The path to match
   * @return the result of matching the path
   */
  def matchPath(path: String): PathMatch = {
    regex.findFirstMatchIn(path) match {
      case Some(m) => {
        val parts = Map(partNames.map {
          case (name, index) => (name, m.group(index))
        }: _*)
        logger.debug(s"Matched path $path against regex $regex resulting in parts $parts")
        PathMatched(parts)
      }
      case None => {
        logger.debug(s"Didn't match path $path against regex $regex")
        PathUnmatched()
      }
    }
  }

  /**
   * Implementation of the ToString method
   * @return the path specification
   */
  override def toString: String = s"Regex=$regex with names $partNames"
}
