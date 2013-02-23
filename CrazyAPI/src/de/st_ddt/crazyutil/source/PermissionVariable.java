package de.st_ddt.crazyutil.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Links a value with the given variable and copies all connected permissions.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
public @interface PermissionVariable {

	String[] variables();

	String[] values();
}
