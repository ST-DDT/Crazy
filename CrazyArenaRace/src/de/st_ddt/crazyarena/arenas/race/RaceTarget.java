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
import de.st_ddt.crazyutil.PlayerCommandable;
import de.st_ddt.crazyutil.poly.room.FuncRoom;
import de.st_ddt.crazyutil.poly.room.Room;
import de.st_ddt.crazyutil.poly.room.Sphere;

public class RaceTarget implements Named, ConfigurationSaveable, PlayerCommandable
{

	protected final RaceArena arena;
	protected String name;
	protected RealRoom<? extends Room> goal;
	protected RaceTarget next;

	public RaceTarget(final RaceArena arena, final String name, final Location center, final double radius, final RaceTarget next)
	{
		super();
		this.arena = arena;
		this.name = name;
		this.goal = new RealRoom<Room>(new Sphere(radius), center);
		this.next = next;
	}

	public RaceTarget(final RaceArena arena, final String name, final RealRoom<Room> goal, final RaceTarget next)
	{
		super();
		this.arena = arena;
		this.name = name;
		this.goal = goal;
		this.next = next;
	}

	public RaceTarget(final RaceArena arena, ConfigurationSection config)
	{
		super();
		this.arena = arena;
		this.name = config.getString("name");
		this.goal = RealRoom.load(config.getConfigurationSection("goal"), null);
		this.next = null;
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

	public RealRoom<? extends Room> getGoal()
	{
		return goal;
	}

	public void setGoal(final RealRoom<Room> goal)
	{
		this.goal = goal;
	}

	public boolean isInside(Entity entity)
	{
		return goal.isInside(entity);
	}

	public boolean isInside(Location location)
	{
		return goal.isInside(location);
	}

	public RaceTarget getNext()
	{
		return next;
	}

	public void setNext(final RaceTarget next)
	{
		this.next = next;
	}

	@Override
	public boolean command(final Player player, final String commandLabel, final String[] args) throws CrazyException
	{
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

	private void CommandName(Player player, String[] args) throws CrazyCommandException
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
				arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, goal.getBasis().getWorld().getName(), goal.getBasis().getX(), goal.getBasis().getY(), goal.getBasis().getZ());
				return;
			case 1:
				String parameter = args[0].toLowerCase();
				if (parameter.equals("set") || parameter.equals("here"))
				{
					goal.setBasis(player.getLocation());
					arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, goal.getBasis().getWorld().getName(), goal.getBasis().getX(), goal.getBasis().getY(), goal.getBasis().getZ());
					return;
				}
				else if (parameter.equals("tp"))
				{
					player.teleport(goal.getBasis(), TeleportCause.COMMAND);
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
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Integer/Double");
				}
				double y;
				try
				{
					y = Double.parseDouble(args[1]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer/Double");
				}
				double z;
				try
				{
					z = Double.parseDouble(args[2]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer/Double");
				}
				goal.setBasis(new Location(world, x, y, z));
				arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, goal.getBasis().getWorld().getName(), goal.getBasis().getX(), goal.getBasis().getY(), goal.getBasis().getZ());
				return;
			case 4:
				try
				{
					x = Double.parseDouble(args[1]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(1, "Integer/Double");
				}
				try
				{
					y = Double.parseDouble(args[2]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(2, "Integer/Double");
				}
				try
				{
					z = Double.parseDouble(args[3]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(3, "Integer/Double");
				}
				world = Bukkit.getWorld(args[0]);
				if (world == null)
					throw new CrazyCommandNoSuchException("World", args[0], player.getWorld().getName());
				goal.setBasis(new Location(world, x, y, z));
				arena.sendLocaleMessage("RACETARGET.COMMAND.CENTER", player, goal.getBasis().getWorld().getName(), goal.getBasis().getX(), goal.getBasis().getY(), goal.getBasis().getZ());
				return;
		}
		throw new CrazyCommandUsageException("/crazyarena targets " + name + " center [set/here/tp]", "/crazyarena targets " + name + " center [World] <X> <Y> <Z>");
	}

	private void CommandRadius(Player player, String[] args) throws CrazyCommandException
	{
		switch (args.length)
		{
			case 0:
				if (goal.getRoom() instanceof Sphere)
					arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, ((Sphere) goal.getRoom()).getRadius());
				else
					arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, "UNKNOWN");
				return;
			case 1:
				double radius;
				try
				{
					radius = Double.parseDouble(args[0]);
				}
				catch (NumberFormatException e)
				{
					throw new CrazyCommandParameterException(0, "Integer/Double");
				}
				goal = new RealRoom<Sphere>(new Sphere(radius), goal.getBasis());
				arena.sendLocaleMessage("RACETARGET.COMMAND.RADIUS", player, ((Sphere) goal.getRoom()).getRadius());
				return;
		}
		throw new CrazyCommandUsageException("/crazyarena targets " + name + " radius [Radius]");
	}

	private void CommandGeo(Player player, String[] args) throws CrazyException
	{
		if (args.length != 1)
			throw new CrazyCommandUsageException("/crazyarena targets " + name + " geo <Get/GetToWE/Set/SetFromWE>");
		String parameter = args[0].toLowerCase();
		CrazyGeo geo = CrazyGeo.getPlugin();
		if (parameter.equals("get"))
		{
			if (goal == null)
				throw new CrazyCommandCircumstanceException("when a goal is set");
			geo.setPlayerSelection(player, goal);
		}
		else if (parameter.equals("gettowe"))
		{
			if (goal == null)
				throw new CrazyCommandCircumstanceException("when a goal is set");
			geo.directExportWE(player, goal);
		}
		else if (parameter.equals("set"))
		{
			RealRoom<Room> sel = geo.getPlayerSelection(player);
			if (sel == null)
				throw new CrazyCommandCircumstanceException("when a region is set");
			else
				goal = sel;
		}
		else if (parameter.equals("setfromwe"))
		{
			RealRoom<FuncRoom> sel = geo.directImportWE(player);
			if (sel == null)
				throw new CrazyCommandCircumstanceException("when a region is set");
			else
				goal = sel;
		}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", name);
		goal.save(config, path + "goal.", true);
	}
}
