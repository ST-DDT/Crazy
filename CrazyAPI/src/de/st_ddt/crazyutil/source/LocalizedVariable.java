package de.st_ddt.crazyutil.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Links a value with the given variable and copies all connected multiligual messages.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
public @interface LocalizedVariable {

	String[] variables();

	String[] values();
}
