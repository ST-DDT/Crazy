package de.st_ddt.crazyarena.rounds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyarena.pve.ArenaPvE;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class RoundTree
{

	protected final ArrayList<Round> rounds = new ArrayList<Round>();
	protected final ArenaPvE arena;

	public RoundTree(ArenaPvE arena)
	{
		super();
		this.arena = arena;
	}

	public Round getRound(int roundNumber)
	{
		Round res = null;
		for (Round round : rounds)
			if (round.isApplyable(roundNumber))
				if (res == null || round.getPriority() >= res.getPriority())
					res = round;
		return res;
	}

	public void load(ConfigurationSection config)
	{
		if (config == null)
			return;
		List<Round> list = ObjectSaveLoadHelper.loadList(config, Round.class, new Class<?>[] { ArenaPvE.class, ConfigurationSection.class }, new Object[] { arena, config }, "de.st_ddt.crazyarena.rounds");
		rounds.addAll(list);
	}
}
