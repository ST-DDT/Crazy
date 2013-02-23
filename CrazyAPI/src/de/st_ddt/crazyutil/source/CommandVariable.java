package de.st_ddt.crazyutil.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Links a value with the given variable and copies all connected commands.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
public @interface CommandVariable {

	String[] variables();

	String[] values();
}
