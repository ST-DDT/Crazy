package de.st_ddt.crazyutil.modules.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.Named;

public interface PermissionSystem extends Named
{

	public boolean hasPermission(CommandSender sender, String permission);

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
