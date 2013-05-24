package de.st_ddt.crazyutil.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method uses the following permissions.<br>
 * Each entry looks like this "permission.$PermissionVar$.permission"
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD })
public @interface Permission {

	String[] value();
}
