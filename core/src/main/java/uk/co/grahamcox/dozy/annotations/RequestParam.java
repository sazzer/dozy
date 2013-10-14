package uk.co.grahamcox.dozy.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a parameter as taking a request parameter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Handler
public @interface RequestParam {
  /** The actual request parameter to use */
  String value();
}


