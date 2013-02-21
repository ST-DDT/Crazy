package de.st_ddt.crazyarena.arenas.pve.rounds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyarena.arenas.pve.PvEArena;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class RoundTree
{

	protected final ArrayList<Round> rounds = new ArrayList<Round>();
	protected final PvEArena arena;

	public RoundTree(final PvEArena arena)
	{
		super();
		this.arena = arena;
	}

	public Round getRound(final int roundNumber)
	{
		Round res = null;
		for (final Round round : rounds)
			if (round.isApplyable(roundNumber))
				if (res == null || round.getPriority() >= res.getPriority())
					res = round;
		return res;
	}

	public void load(final ConfigurationSection config)
	{
		if (config == null)
			return;
		final List<Round> list = ObjectSaveLoadHelper.loadList(config, Round.class, new Class<?>[] { PvEArena.class, ConfigurationSection.class }, new Object[] { arena, config }, "de.st_ddt.crazyarena.rounds");
		rounds.addAll(list);
	}
}
