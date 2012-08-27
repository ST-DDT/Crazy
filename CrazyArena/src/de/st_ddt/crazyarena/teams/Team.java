package de.st_ddt.crazyarena.teams;

import java.util.HashSet;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.exceptions.CrazyArenaTeamExceedingMemberLimitException;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyutil.Named;

public class Team<S extends Participant<S, T>, T extends Arena<S>> implements Named
{

	protected final T arena;
	protected final TreeSet<String> members = new TreeSet<String>();
	protected final String name;
	private int maxSize;
	protected ChatColor color = null;
	private boolean autosetcolors = false;

	public Team(T arena, String name, int maxSize)
	{
		super();
		this.arena = arena;
		this.name = name;
		this.maxSize = maxSize;
	}

	public String getName()
	{
		return name;
	}

	public T getArena()
	{
		return arena;
	}

	public void setMaxSize(int maxSize)
	{
		this.maxSize = maxSize;
	}

	public int getMaxSize()
	{
		return maxSize;
	}

	public void setAutoColors(boolean autosetcolors)
	{
		this.autosetcolors = autosetcolors;
		if (autosetcolors)
			setTeamColor();
	}

	public void add(Player player) throws CrazyArenaTeamExceedingMemberLimitException
	{
		if (isMember(player))
			return;
		if (maxSize > 0 && maxSize <= members.size())
			throw new CrazyArenaTeamExceedingMemberLimitException(arena, this, maxSize);
		members.add(player.getName().toLowerCase());
		if (autosetcolors)
			setTeamColor(player);
	}

	public void remove(Player player)
	{
		members.remove(player.getName().toLowerCase());
	}

	public TreeSet<String> getMembers()
	{
		return members;
	}

	public HashSet<Player> getMemberPlayers()
	{
		HashSet<Player> players = new HashSet<Player>();
		for (String name : getMembers())
			players.add(Bukkit.getPlayerExact(name));
		players.remove(null);
		return players;
	}

	public HashSet<S> getMemberParticipants()
	{
		HashSet<S> participants = new HashSet<S>();
		for (String name : getMembers())
			participants.add(arena.getParticipant(name));
		participants.remove(null);
		return participants;
	}

	public boolean isMember(Player player)
	{
		return members.contains(player.getName().toLowerCase());
	}

	public void clear()
	{
		members.clear();
	}

	public int getMemberCount()
	{
		return members.size();
	}

	public ChatColor getColor()
	{
		return color;
	}

	public void setColor(ChatColor color)
	{
		this.color = color;
		if (autosetcolors)
			setTeamColor();
	}

	public void setTeamColor()
	{
		for (Player player : getMemberPlayers())
			setTeamColor(player);
	}

	public void setTeamColor(Player player)
	{
		ItemStack helmet = null;
		switch (color)
		{
			case WHITE:
				helmet = new ItemStack(35, 1, (short) 0);
				break;
			case RED:
				helmet = new ItemStack(35, 1, (short) 1);
				break;
			case MAGIC:
				helmet = new ItemStack(35, 1, (short) 2);
				break;
			case BLUE:
				helmet = new ItemStack(35, 1, (short) 3);
				break;
			case YELLOW:
				helmet = new ItemStack(35, 1, (short) 4);
				break;
			case GREEN:
				helmet = new ItemStack(35, 1, (short) 5);
				break;
			case LIGHT_PURPLE:
				helmet = new ItemStack(35, 1, (short) 6);
				break;
			case DARK_GRAY:
				helmet = new ItemStack(35, 1, (short) 7);
				break;
			case GRAY:
				helmet = new ItemStack(35, 1, (short) 8);
				break;
			case AQUA:
				helmet = new ItemStack(35, 1, (short) 9);
				break;
			case DARK_PURPLE:
				helmet = new ItemStack(35, 1, (short) 10);
				break;
			case DARK_BLUE:
				helmet = new ItemStack(35, 1, (short) 11);
				break;
			case GOLD:
				helmet = new ItemStack(35, 1, (short) 12);
				break;
			case DARK_GREEN:
				helmet = new ItemStack(35, 1, (short) 13);
				break;
			case DARK_RED:
				helmet = new ItemStack(35, 1, (short) 14);
				break;
			case BLACK:
				helmet = new ItemStack(35, 1, (short) 15);
				break;
			default:
				helmet = null;
		}
		player.getInventory().setHelmet(helmet);
	}
}
