package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class VillagerProperty extends BasicProperty
{

	protected final Profession profession;

	public VillagerProperty()
	{
		super();
		this.profession = null;
	}

	public VillagerProperty(final ConfigurationSection config)
	{
		super(config);
		final String professionName = config.getString("profession");
		if (professionName == null)
			this.profession = null;
		else
		{
			Profession profession = null;
			try
			{
				profession = Profession.valueOf(professionName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s profession " + professionName + " was corrupted/invalid and has been removed!");
			}
			this.profession = profession;
		}
	}

	@SuppressWarnings("unchecked")
	public VillagerProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final EnumParamitrisable<Profession> professionParam = (EnumParamitrisable<Profession>) params.get("profession");
		this.profession = professionParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Villager villager = (Villager) entity;
		if (villager != null)
			villager.setProfession(profession);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final EnumParamitrisable<Profession> professionParam = new EnumParamitrisable<Profession>("Profession", profession, Profession.values());
		params.put("p", professionParam);
		params.put("profession", professionParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "profession", profession.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "profession", "Profession");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.PROFESSION $Profession$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PROFESSION", target, profession == null ? "Default" : profession.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return profession == null;
	}
}
