package de.st_ddt.crazyarena.arenas.race;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyarena.participants.race.RaceParticipant;
import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.poly.room.Room;
import de.st_ddt.crazyutil.poly.room.Sphere;

public class RaceStage implements Named, ConfigurationSaveable
{

	protected final RaceArena arena;
	protected String name;
	protected RealRoom<? extends Room> zone;
	protected RaceStage next;
	protected final List<RaceData> datas = new ArrayList<RaceData>();

	public RaceStage(final RaceArena arena, final String name, final Location center, final double radius, final RaceStage next)
	{
		this(arena, name, new RealRoom<Room>(new Sphere(radius), center), next);
	}

	public RaceStage(final RaceArena arena, final ConfigurationSection config)
	{
		this(arena, config.getString("name"), RealRoom.load(config.getConfigurationSection("zone"), null));
	}

	public RaceStage(final RaceArena arena, final String name, final RealRoom<?> zone)
	{
		this(arena, name, zone, null);
	}

	public RaceStage(final RaceArena arena, final String name, final RealRoom<?> zone, final RaceStage next)
	{
		super();
		this.arena = arena;
		this.name = name;
		this.zone = zone;
		this.next = next;
	}

	public RaceArena getArena()
	{
		return arena;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public RealRoom<? extends Room> getZone()
	{
		return zone;
	}

	public void setZone(final RealRoom<? extends Room> zone)
	{
		this.zone = zone;
	}

	public boolean isInside(final Entity entity)
	{
		return zone.isInside(entity);
	}

	public boolean isInside(final Location location)
	{
		return zone.isInside(location);
	}

	public RaceStage getNext()
	{
		return next;
	}

	public void setNext(final RaceStage next)
	{
		this.next = next;
	}

	public boolean isGoal()
	{
		return next == null;
	}

	public List<RaceData> getDatas()
	{
		return datas;
	}

	public RaceData reachStage(final RaceParticipant participant, final long time)
	{
		final RaceData data = new RaceData(arena, this, participant, datas.size() + 1, time);
		datas.add(data);
		return data;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", name);
		zone.save(config, path + "zone.", true);
	}

	@Override
	public String toString()
	{
		if (isGoal())
			return toShortString() + " {Zone: " + zone.toString() + "}";
		else
			return toShortString() + " {Zone: " + zone.toString() + "; Next: " + next.toShortString() + "}";
	}

	public String toShortString()
	{
		if (name == null)
			if (isGoal())
				return "RaceGoal";
			else
				return "RaceStage";
		else if (isGoal())
			return name + " (Goal)";
		else
			return name + " (Stage)";
	}
}
