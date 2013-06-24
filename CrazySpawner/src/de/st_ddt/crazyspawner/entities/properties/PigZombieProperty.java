package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class PigZombieProperty extends BasicProperty
{

	protected final boolean angry;

	public PigZombieProperty()
	{
		super();
		this.angry = false;
	}

	public PigZombieProperty(final ConfigurationSection config)
	{
		super(config);
		this.angry = config.getBoolean("angry", false);
	}

	public PigZombieProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable angryParam = (BooleanParamitrisable) params.get("angry");
		this.angry = angryParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final PigZombie pigzombie = (PigZombie) entity;
		pigzombie.setAngry(angry);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable angryParam = new BooleanParamitrisable(angry);
		params.put("a", angryParam);
		params.put("angry", angryParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "angry", angry);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "angry", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.ANGRY $Angry$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ANGRY", target, angry);
	}
}
