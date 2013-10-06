package uk.co.grahamcox.dozy.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a Dozy Controller
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Handler
public @interface Path {
  /** The actual path value to use */
  String value();
}

