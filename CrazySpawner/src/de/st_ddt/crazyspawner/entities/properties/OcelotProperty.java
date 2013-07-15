package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class OcelotProperty extends BasicProperty
{

	protected final Type type;
	protected final boolean sitting;

	public OcelotProperty()
	{
		super();
		this.type = null;
		this.sitting = false;
	}

	public OcelotProperty(final ConfigurationSection config)
	{
		super(config);
		final String typeName = config.getString("catType");
		if (typeName == null)
			this.type = null;
		else
		{
			Type type = null;
			try
			{
				type = Type.valueOf(typeName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s ocelot/cat type " + typeName + " was corrupted/invalid and has been removed!");
			}
			this.type = type;
		}
		this.sitting = config.getBoolean("sitting", false);
	}

	@SuppressWarnings("unchecked")
	public OcelotProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final EnumParamitrisable<Type> typeParam = (EnumParamitrisable<Type>) params.get("ocelottype");
		this.type = typeParam.getValue();
		final BooleanParamitrisable sittingParam = (BooleanParamitrisable) params.get("sitting");
		this.sitting = sittingParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Ocelot ocelot = (Ocelot) entity;
		if (type != null)
			ocelot.setCatType(type);
		ocelot.setSitting(sitting);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final EnumParamitrisable<Type> typeParam = new EnumParamitrisable<Type>("CatType", type, Type.values());
		params.put("ctype", typeParam);
		params.put("cattype", typeParam);
		params.put("ocelottype", typeParam);
		final BooleanParamitrisable sittingParam = new BooleanParamitrisable(sitting);
		params.put("sit", sittingParam);
		params.put("sitting", sittingParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (type != null)
			config.set(path + "catType", type.name());
		config.set(path + "sitting", sitting);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "catType", "CatType");
		config.set(path + "sitting", "boolean (true/false)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.CATTYPE $CatType$", "CRAZYSPAWNER.ENTITY.PROPERTY.SITTING $Sitting$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.CATTYPE", target, type == null ? "Default" : type.name());
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SITTING", target, sitting);
	}

	@Override
	public boolean equalsDefault()
	{
		return type == null && sitting == false;
	}
}
