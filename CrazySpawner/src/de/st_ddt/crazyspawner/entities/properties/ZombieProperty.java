package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class ZombieProperty extends BasicProperty
{

	protected final boolean baby;
	protected final boolean villager;

	public ZombieProperty()
	{
		super();
		this.baby = false;
		this.villager = false;
	}

	public ZombieProperty(final ConfigurationSection config)
	{
		super(config);
		this.baby = config.getBoolean("baby", false);
		this.villager = config.getBoolean("villager", false);
	}

	public ZombieProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable babyParam = (BooleanParamitrisable) params.get("baby");
		this.baby = babyParam.getValue();
		final BooleanParamitrisable villagerParam = (BooleanParamitrisable) params.get("villager");
		this.villager = villagerParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Zombie zombie = (Zombie) entity;
		zombie.setBaby(baby);
		zombie.setVillager(villager);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable babyParam = new BooleanParamitrisable(baby);
		params.put("b", babyParam);
		params.put("baby", babyParam);
		final BooleanParamitrisable villagerParam = new BooleanParamitrisable(villager);
		params.put("v", villagerParam);
		params.put("villager", villagerParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "baby", baby);
		config.set(path + "villager", villager);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "baby", "boolean (true/false)");
		config.set(path + "villager", "boolean (true/false)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.BABY $Baby$", "CRAZYSPAWNER.ENTITY.PROPERTY.VILLAGER $Villager$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.BABY", target, baby);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.VILLAGER", target, villager);
	}

	@Override
	public boolean equalsDefault()
	{
		return baby == false && villager == false;
	}
}
