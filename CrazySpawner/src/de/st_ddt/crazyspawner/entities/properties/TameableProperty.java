package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.OfflinePlayerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class TameableProperty extends BasicProperty
{

	protected final OfflinePlayer tamer;
	protected final boolean tamed;

	public TameableProperty()
	{
		super();
		this.tamer = null;
		this.tamed = false;
	}

	public TameableProperty(final ConfigurationSection config)
	{
		super(config);
		final String tamer = config.getString("tamer");
		if (tamer == null)
			this.tamer = null;
		else
			this.tamer = Bukkit.getOfflinePlayer(tamer);
		this.tamed = tamer != null || config.getBoolean("tamed", false);
	}

	public TameableProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final OfflinePlayerParamitrisable tamerParam = (OfflinePlayerParamitrisable) params.get("tamer");
		this.tamer = tamerParam.getValue();
		final BooleanParamitrisable tamedParam = (BooleanParamitrisable) params.get("tamed");
		this.tamed = tamer != null || tamedParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Tameable tameable = (Tameable) entity;
		tameable.setTamed(tamed);
		if (tamer != null)
			tameable.setOwner(tamer);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final OfflinePlayerParamitrisable tamerParam = new OfflinePlayerParamitrisable(tamer);
		params.put("t", tamerParam);
		params.put("tamer", tamerParam);
		final BooleanParamitrisable tamedParam = new BooleanParamitrisable(tamed);
		params.put("tamed", tamedParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (tamer == null)
			config.set(path + "tamed", tamed);
		else
			config.set(path + "tamer", tamer.getName());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "tamer", "Player");
		config.set(path + "tamed", "boolean (true/false)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.TAMED $Tamed$", "CRAZYSPAWNER.ENTITY.PROPERTY.TAMER $Tamer$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.TAMED", target, tamed);
		if (tamed)
			CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.TAMER", target, tamer == null ? "SERVER" : tamer);
	}

	@Override
	public boolean equalsDefault()
	{
		return tamed == false;
	}
}
