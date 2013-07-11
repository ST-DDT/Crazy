package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class SkeletonProperty extends BasicProperty
{

	protected final SkeletonType type;

	public SkeletonProperty()
	{
		super();
		this.type = null;
	}

	public SkeletonProperty(final ConfigurationSection config)
	{
		super(config);
		final String typeName = config.getString("skeletonType");
		if (typeName == null)
			this.type = null;
		else
		{
			SkeletonType type = null;
			try
			{
				type = SkeletonType.valueOf(typeName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s skeleton type " + typeName + " was corrupted/invalid and has been removed!");
			}
			this.type = type;
		}
	}

	@SuppressWarnings("unchecked")
	public SkeletonProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final EnumParamitrisable<SkeletonType> typeParam = (EnumParamitrisable<SkeletonType>) params.get("skeletontype");
		this.type = typeParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Skeleton skeleton = (Skeleton) entity;
		if (skeleton != null)
			skeleton.setSkeletonType(type);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final EnumParamitrisable<SkeletonType> typeParam = new EnumParamitrisable<SkeletonType>("SkeletonType", type, SkeletonType.values());
		params.put("stype", typeParam);
		params.put("skeletontype", typeParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "skeletonType", type.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "skeletonType", "SkeletonType");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.SKELETONTYPE $SkeletonType$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SKELETONTYPE", target, type.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return type == null;
	}
}
