package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.ChatConverter;
import de.st_ddt.crazyutil.paramitrisable.DurationParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class DespawnProperty extends BasicProperty
{

	/**
	 * Entities will despawn after x Ticks
	 */
	protected final long despawnAfter;

	public DespawnProperty()
	{
		super();
		this.despawnAfter = -1;
	}

	public DespawnProperty(final long despawnAfter)
	{
		super();
		this.despawnAfter = getSecureValue(despawnAfter);
	}

	public DespawnProperty(final ConfigurationSection config)
	{
		super(config);
		this.despawnAfter = getSecureValue(config.getLong("despawnAfter", -1));
	}

	public DespawnProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DurationParamitrisable despawnAfterParam = (DurationParamitrisable) params.get("despawnafter");
		this.despawnAfter = getSecureValue(despawnAfterParam.getTicks());
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return !LivingEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (despawnAfter > 0)
			Bukkit.getScheduler().scheduleSyncDelayedTask(CrazySpawner.getPlugin(), new Runnable()
			{

				@Override
				public void run()
				{
					if (entity.isValid())
						entity.remove();
				}
			}, despawnAfter);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DurationParamitrisable despawnAfterParam = new DurationParamitrisable(despawnAfter * 50);
		params.put("da", despawnAfterParam);
		params.put("dafter", despawnAfterParam);
		params.put("despawnafter", despawnAfterParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "despawnAfter", despawnAfter);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "despawnAfter", "Ticks (-1 = disabled)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.DESPAWNAFTER $DespawnAfter$", "CRAZYSPAWNER.ENTITY.PROPERTY.DESPAWNAFTER.DISABLED" })
	public void show(final CommandSender target)
	{
		if (despawnAfter > 0)
			CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DESPAWNAFTER", target, ChatConverter.timeConverter(despawnAfter * 50, 2, target, -1, false, true));
		else
			CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DESPAWNAFTER.DISABLED", target);
	}

	@Override
	public boolean equalsDefault()
	{
		return despawnAfter == -1;
	}
}
