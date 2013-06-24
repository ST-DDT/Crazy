package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.NamedEntitySpawner;
import de.st_ddt.crazyutil.paramitrisable.NamedEntitySpawnerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class PassengerProperty extends BasicProperty
{

	protected final NamedEntitySpawner passenger;

	public PassengerProperty()
	{
		super();
		this.passenger = null;
	}

	public PassengerProperty(final ConfigurationSection config)
	{
		super(config);
		this.passenger = NamedEntitySpawnerParamitrisable.getNamedEntitySpawner(config.getString("passenger", null));
	}

	public PassengerProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final NamedEntitySpawnerParamitrisable passengerParam = (NamedEntitySpawnerParamitrisable) params.get("passenger");
		this.passenger = passengerParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		if (passenger != null)
			entity.setPassenger(passenger.spawn(entity.getLocation()));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final NamedEntitySpawnerParamitrisable passengerParam = new NamedEntitySpawnerParamitrisable(passenger);
		params.put("p", passengerParam);
		params.put("passenger", passengerParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (passenger == null)
			config.set(path + "passenger", null);
		else
			config.set(path + "passenger", passenger.getName());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "passenger", "NamedEntitySpawner");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.PASSENGER $Passenger$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PASSENGER", target, passenger == null ? "None" : passenger.getName());
	}
}
