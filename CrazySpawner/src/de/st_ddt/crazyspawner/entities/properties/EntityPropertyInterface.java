package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;

public interface EntityPropertyInterface extends ConfigurationSaveable
{

	public void apply(Entity entity);

	public void show(CommandSender target);

	public void getCommandParams(Map<String, ? super TabbedParamitrisable> params);

	public void dummySave(ConfigurationSection config, String path);
}
