package uk.co.grahamcox.dozy.annotations;

import java.lang.annotation.*;

/**
 * Annotation to mark a method as a Handler, or else to mark another annotation as one that marks a handler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Handler {
}

