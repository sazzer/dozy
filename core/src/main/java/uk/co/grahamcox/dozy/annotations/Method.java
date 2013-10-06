package uk.co.grahamcox.dozy.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a class or handler method with the HTTP Method it supports
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Handler
public @interface Method {
  /** The actual HTTP Method to use */
  String value();
}


