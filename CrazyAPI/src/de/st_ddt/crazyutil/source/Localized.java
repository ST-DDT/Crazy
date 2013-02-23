package de.st_ddt.crazyutil.source;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method uses multiligual messages defined by the listed paths.<br>
 * Each entry looks like this "PATH.$PathVar$.PATH $Var1$ $Var2$ ..."
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface Localized {

	String[] value();
}
