package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner.LightningSpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class LightningProperty extends LightningSpawner implements EntityPropertyInterface
{

	public LightningProperty()
	{
		super();
	}

	public LightningProperty(final ConfigurationSection config)
	{
		super(config.getBoolean("effect", false));
	}

	public LightningProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(((BooleanParamitrisable) params.get("effect")).getValue());
	}

	@Override
	public void apply(final Entity entity)
	{
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable effectParam = new BooleanParamitrisable(effect);
		params.put("e", effectParam);
		params.put("effect", effectParam);
		params.put("lightningeffect", effectParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "effect", effect);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "effect", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.LIGHTNINGEFFECT $Effect$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.LIGHTNINGEFFECT", target, effect);
	}

	@Override
	public boolean equalsDefault()
	{
		return effect == false;
	}
}
