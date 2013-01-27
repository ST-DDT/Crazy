package de.st_ddt.crazyarena.arenas.race;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import de.st_ddt.crazygeo.CrazyGeo;
import de.st_ddt.crazygeo.region.RealRoom;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.poly.room.FuncRoom;
import de.st_ddt.crazyutil.poly.room.Room;
import de.st_ddt.crazyutil.poly.room.Sphere;

public class RaceStage implements Named, ConfigurationSaveable
{

	protected final RaceArena arena;
	protected String name;
	protected RealRoom<? extends Room> zone;
	protected RaceStage next;

	public RaceStage(final RaceArena arena, final String name, final Location center, final double radius, final RaceStage next)
	{
		this(arena, name, new RealRoom<Room>(new Sphere(radius), center));
	}

	public RaceStage(final RaceArena arena, final ConfigurationSection config)
	{
		this(arena, config.getString("name"), RealRoom.load(config.getConfigurationSection("goal"), null));
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

	public void setZone(final RealRoom<Room> zone)
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

	public boolean command(final Player player, final String commandLabel, final String[] args) throws CrazyException
	{
		// EDIT Outsourcen!
		if (commandLabel.equals("name"))
		{
			CommandName(player, args);
			return true;
		}
		else if (commandLabel.equals("center"))
		{
			CommandCenter(player, args);
			return true;
		}
		else if (commandLabel.equals("radius"))
		{
			CommandRadius(player, args);
			return true;
		}
		else if (commandLabel.equals("geo"))
		{
			CommandGeo(player, args);
			return true;
		}
		return false;
	}

	private void CommandName(final Player player, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				arena.sendLocaleMessage("RACETARGET.COMMAND.Name", player, name);
				return;
			case 1:
				name = args[1];
				arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, name);
				return;
		}
		throw new CrazyCommandUsageException("/crazyarena targets " + name + " name [Name]");
	}

	private void CommandCenter(final Player player, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, zone.getBasis().getWorld().getName(), zone.getBasis().getX(), zone.getBasis().getY(), zone.getBasis().getZ());
				return;
			case 1:
				final String parameter = args[0].toLowerCase();
				if (parameter.equals("set") || parameter.equals("here"))
				{
					zone.setBasis(player.getLocation());
					arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, zone.getBasis().getWorld().getName(), zone.getBasis().getX(), zone.getBasis().getY(), zone.getBasis().getZ());
					return;
				}
				else if (parameter.equals("tp"))
				{
					player.teleport(zone.getBasis(), TeleportCause.COMMAND);
					return;
				}
				else
					break;
			case 3:
				World world = player.getWorld();
				double x;
				try
				{
					x = Double.parseDouble(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Integer/Double");
				}
				double y;
				try
				{
					y = Double.parseDouble(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer/Double");
				}
				double z;
				try
				{
					z = Double.parseDouble(args[2]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer/Double");
				}
				zone.setBasis(new Location(world, x, y, z));
				arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, zone.getBasis().getWorld().getName(), zone.getBasis().getX(), zone.getBasis().getY(), zone.getBasis().getZ());
				return;
			case 4:
				try
				{
					x = Double.parseDouble(args[1]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer/Double");
				}
				try
				{
					y = Double.parseDouble(args[2]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer/Double");
				}
				try
				{
					z = Double.parseDouble(args[3]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(3, "Integer/Double");
				}
				world = Bukkit.getWorld(args[0]);
				if (world == null)
					throw new CrazyCommandNoSuchException("World", args[0], player.getWorld().getName());
				zone.setBasis(new Location(world, x, y, z));
				arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, zone.getBasis().getWorld().getName(), zone.getBasis().getX(), zone.getBasis().getY(), zone.getBasis().getZ());
				return;
		}
		throw new CrazyCommandUsageException("/crazyarena targets " + name + " center [set/here/tp]", "/crazyarena targets " + name + " center [World] <X> <Y> <Z>");
	}

	private void CommandRadius(final Player player, final String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				if (zone.getRoom() instanceof Sphere)
					arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, ((Sphere) zone.getRoom()).getRadius());
				else
					arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, "UNKNOWN");
				return;
			case 1:
				double radius;
				try
				{
					radius = Double.parseDouble(args[0]);
				}
				catch (final NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Integer/Double");
				}
				zone = new RealRoom<Sphere>(new Sphere(radius), zone.getBasis());
				arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, ((Sphere) zone.getRoom()).getRadius());
				return;
		}
		throw new CrazyCommandUsageException("/crazyarena targets " + name + " radius [Radius]");
	}

	private void CommandGeo(final Player player, final String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyarena targets " + name + " geo <Get/GetToWE/Set/SetFromWE>");
		final String parameter = args[0].toLowerCase();
		final CrazyGeo geo = CrazyGeo.getPlugin();
		if (parameter.equals("get"))
		{
			if (zone == null)
				throw new CrazyCommandCircumstanceException("when a goal is set");
			geo.setPlayerSelection(player, zone);
		}
		else if (parameter.equals("gettowe"))
		{
			if (zone == null)
				throw new CrazyCommandCircumstanceException("when a goal is set");
			geo.directExportWE(player, zone);
		}
		else if (parameter.equals("set"))
		{
			final RealRoom<Room> sel = geo.getPlayerSelection(player);
			if (sel == null)
				throw new CrazyCommandCircumstanceException("when a region is set");
			else
				zone = sel;
		}
		else if (parameter.equals("setfromwe"))
		{
			final RealRoom<FuncRoom> sel = geo.directImportWE(player);
			if (sel == null)
				throw new CrazyCommandCircumstanceException("when a region is set");
			else
				zone = sel;
		}
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
