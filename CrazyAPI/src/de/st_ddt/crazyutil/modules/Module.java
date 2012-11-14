package de.st_ddt.crazyutil.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.Named;

public interface Module extends Named
{

	static final Map<String, Module> MODULES = new HashMap<String, Module>();

	public boolean isActive();

	public boolean initialize(final String chatHeader, final CommandSender sender);

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface PluginDepency {

		String depend();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Named {

		String name();
	}
}
