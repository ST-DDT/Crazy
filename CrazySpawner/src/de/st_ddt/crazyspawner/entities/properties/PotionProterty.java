package de.st_ddt.crazyspawner.entities.properties;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class PotionProterty extends BasicProperty
{

	private final static int POTIONDURATION = 20 * 60 * 60 * 24;
	private final static Comparator<PotionEffectType> POTIONEFFECTTYPE_COMCOMPARATOR = new Comparator<PotionEffectType>()
	{

		@Override
		public int compare(final PotionEffectType o1, final PotionEffectType o2)
		{
			return o1.getName().compareTo(o2.getName());
		}
	};
	protected final Map<PotionEffectType, Integer> potionEffects = new TreeMap<PotionEffectType, Integer>(POTIONEFFECTTYPE_COMCOMPARATOR);

	public PotionProterty()
	{
		super();
	}

	public PotionProterty(final Map<PotionEffectType, Integer> potions, final boolean dummy)
	{
		super();
		potionEffects.putAll(potions);
	}

	public PotionProterty(final PotionEffectType[] effects, final int[] levels)
	{
		super();
		if (effects.length != levels.length)
			throw new IllegalArgumentException("PotionEffects and Levels must have the same length!");
		for (int i = 0; i < effects.length; i++)
			potionEffects.put(effects[i], levels[i]);
	}

	public PotionProterty(final ConfigurationSection config)
	{
		super(config);
		final ConfigurationSection potionConfig = config.getConfigurationSection("potionEffects");
		if (potionConfig != null)
			for (final String key : potionConfig.getKeys(false))
			{
				final PotionEffectType potionType = PotionEffectType.getByName(key);
				if (potionType == null)
					continue;
				final int level = potionConfig.getInt(key, 1);
				if (level > 0)
					potionEffects.put(potionType, level);
			}
	}

	public PotionProterty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		for (final PotionEffectType type : PotionEffectType.values())
			if (type != null)
			{
				final IntegerParamitrisable levelParam = (IntegerParamitrisable) params.get("potion" + type.getName().toLowerCase());
				final Integer level = levelParam.getValue();
				if (level == null)
					continue;
				if (level > 0)
					potionEffects.put(type, level);
			}
	}

	@Override
	public void apply(final Entity entity)
	{
		final LivingEntity living = (LivingEntity) entity;
		for (final Entry<PotionEffectType, Integer> entry : potionEffects.entrySet())
			living.addPotionEffect(new PotionEffect(entry.getKey(), POTIONDURATION, entry.getValue()));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		for (final PotionEffectType type : PotionEffectType.values())
			if (type != null)
			{
				final IntegerParamitrisable level = new IntegerParamitrisable(potionEffects.get(type));
				params.put("p" + type.getName().toLowerCase(), level);
				params.put("potion" + type.getName().toLowerCase(), level);
			}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		for (final Entry<PotionEffectType, Integer> entry : potionEffects.entrySet())
			if (entry.getValue() > 0)
				config.set(path + "potionEffects." + entry.getKey().getName(), entry.getValue());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "potionEffects.PotionEffectType1", "int");
		config.set(path + "potionEffects.PotionEffectType2", "int");
		config.set(path + "potionEffects.PotionEffectTypeX", "int");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.POTION $PotionEffectType$ $Level$")
	public void show(final CommandSender target)
	{
		for (final Entry<PotionEffectType, Integer> entry : potionEffects.entrySet())
			if (entry.getValue() > 0)
				CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.POTION", target, entry.getKey().getName(), entry.getValue());
	}

	@Override
	public boolean equalsDefault()
	{
		return potionEffects == null;
	}
}
