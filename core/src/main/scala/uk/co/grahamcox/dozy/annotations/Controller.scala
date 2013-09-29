package uk.co.grahamcox.dozy.annotations

import scala.annotation.StaticAnnotation

/**
 * Annotation to mark a Dozy Controller as such
 */
case class Controller() extends StaticAnnotation
