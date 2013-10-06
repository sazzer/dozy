package uk.co.grahamcox.dozy.servlet

import javax.servlet.ServletConfig
import org.specs2.mutable._
import scala.collection.JavaConverters._
import uk.co.grahamcox.dozy.annotations._
import uk.co.grahamcox.dozy.annotations.{Handler => HandlerMethod}

class PathMatcherSpec extends Specification {
  "The path matcher" should {
    "with the path specfication '/a/b/c'" in {
      val matcher = new PathMatcher("/a/b/c")
      "matching the incoming path '/a/b/c'" in {
        val matched = matcher.matchPath("/a/b/c")
        "return a matched path" in {
          matched must beAnInstanceOf[PathMatched]
        }

        "with no subparts" in {
          matched match {
            case PathMatched(parts) => {
              parts must be empty

              success
            }
            case _ => skipped
          }
        }
      }
      "matching the incoming path '/c/b/a'" in {
        notMatchingPath(matcher, "/c/b/a")
      }
      "matching the incoming path '/a/b/d'" in {
        notMatchingPath(matcher, "/a/b/d")
      }
      "matching the incoming path '/a/b'" in {
        notMatchingPath(matcher, "/a/b")
      }
    }
    "with the path specfication '/a/:b/:c'" in {
      val matcher = new PathMatcher("/a/:b/:c")
      "matching the incoming path '/a/b/c'" in {
        val matched = matcher.matchPath("/a/b/c")
        "return a matched path" in {
          matched must beAnInstanceOf[PathMatched]
        }
        "with two subparts" in {
          matched match {
            case PathMatched(parts) => {
              parts must have size(2)
              parts must havePair("b" -> "b")
              parts must havePair("c" -> "c")
              success
            }
            case _ => skipped
          }
        }
      }
      "matching the incoming path '/a/hello/world'" in {
        val matched = matcher.matchPath("/a/hello/world")
        "return a matched path" in {
          matched must beAnInstanceOf[PathMatched]
        }
        "with two subparts" in {
          matched match {
            case PathMatched(parts) => {
              parts must have size(2)
              parts must havePair("b" -> "hello")
              parts must havePair("c" -> "world")
              success
            }
            case _ => skipped
          }
        }
      }
      "matching the incoming path '/c/b/a'" in {
        notMatchingPath(matcher, "/c/b/a")
      }
      "matching the incoming path '/a/b'" in {
        notMatchingPath(matcher, "/a/b")
      }
      "matching the incoming path '/a/b/c/d'" in {
        notMatchingPath(matcher, "/a/b/c/d")
      }
    }
  }

  def notMatchingPath(matcher: PathMatcher, path: String) = {
    "return an unmatched path" in {
      val matched = matcher.matchPath(path)
      matched must beAnInstanceOf[PathUnmatched]
    }
  }
}




