package de.st_ddt.crazyutil.modules;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.Named;

public interface Module extends Named
{

	static final Map<String, Module> MODULES = new HashMap<String, Module>();

	public boolean isActive();

	public boolean initialize(final String chatHeader, final CommandSender sender);
}
