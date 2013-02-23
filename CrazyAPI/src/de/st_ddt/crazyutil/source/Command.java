package de.st_ddt.crazyutil.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method uses multiligual messages defined by the listed paths.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface Command {

	String[] commands();

	String[] usage() default "";
}
