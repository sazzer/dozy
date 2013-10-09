package uk.co.grahamcox.dozy.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a parameter as taking a path parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Handler
public @interface PathParam {
  /** The actual path parameter to use */
  String value();
}


