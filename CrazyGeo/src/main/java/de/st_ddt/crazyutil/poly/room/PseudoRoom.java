package de.st_ddt.crazyutil.poly.room;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

/**
 * All Subclasses have to implement this constructor: <br>
 * public PseudoRoom(final ConfigurationSection config);
 */
public abstract class PseudoRoom implements Room
{

	public static Room load(final ConfigurationSection config)
	{
		return ObjectSaveLoadHelper.load(config, Room.class, new Class[] { ConfigurationSection.class }, new Object[] { config });
	}

	@Override
	public final void save(final ConfigurationSection config, final String path, final boolean includeType)
	{
		if (includeType)
			config.set(path + "type", getClass().getName());
		save(config, path);
	}

	@Override
	public abstract PseudoRoom clone();

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}
}
