package de.st_ddt.crazysquads.commands;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazysquads.CrazySquads;
import de.st_ddt.crazysquads.data.Loot_Rules;
import de.st_ddt.crazysquads.data.Squad;
import de.st_ddt.crazysquads.data.XP_Rules;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazySquadsSquadPlayerCommandSquadMode extends CrazySquadsSquadPlayerCommandExecutor
{

	protected final Map<String, Mode<?>> modes = new TreeMap<String, Mode<?>>();

	public CrazySquadsSquadPlayerCommandSquadMode(final CrazySquads plugin)
	{
		super(plugin);
		addMode(new Mode<Loot_Rules>("Loot", Loot_Rules.class)
		{

			@Override
			public Loot_Rules getValue(final Squad squad)
			{
				return squad.getLootRule();
			}

			@Override
			public void setValue(final Player player, final Squad squad, final String... args) throws CrazyException
			{
				final String name = args[0].toUpperCase();
				final Loot_Rules rule = Loot_Rules.valueOf(name);
				if (rule == null)
					throw new CrazyCommandNoSuchException("Loot_Rule", name);
				setValue(squad, rule);
				showValue(player, squad);
			}

			@Override
			public void setValue(final Squad squad, final Loot_Rules newValue) throws CrazyException
			{
				squad.setLootRule(newValue);
			}

			@Override
			public List<String> tab(final Squad squad, final String... args)
			{
				if (args.length != 1)
					return null;
				final List<String> res = new LinkedList<String>();
				for (final Loot_Rules rule : Loot_Rules.values())
					if (rule.toString().startsWith(args[0].toUpperCase()))
						res.add(rule.toString());
				return res;
			}
		});
		addMode(new Mode<XP_Rules>("XP", XP_Rules.class)
		{

			@Override
			public XP_Rules getValue(final Squad squad)
			{
				return squad.getXPRule();
			}

			@Override
			public void setValue(final Player player, final Squad squad, final String... args) throws CrazyException
			{
				final String name = args[0].toUpperCase();
				final XP_Rules rule = XP_Rules.valueOf(name);
				if (rule == null)
					throw new CrazyCommandNoSuchException("XP_Rule", name);
				setValue(squad, rule);
				showValue(player, squad);
			}

			@Override
			public void setValue(final Squad squad, final XP_Rules newValue) throws CrazyException
			{
				squad.setXPRule(newValue);
			}

			@Override
			public List<String> tab(final Squad squad, final String... args)
			{
				if (args.length != 1)
					return null;
				final List<String> res = new LinkedList<String>();
				for (final XP_Rules rule : XP_Rules.values())
					if (rule.toString().startsWith(args[0].toUpperCase()))
						res.add(rule.toString());
				return res;
			}
		});
	}

	@Override
	public void command(final Player player, final Squad squad, final String[] args) throws CrazyException
	{
		if (args.length == 0)
			throw new CrazyCommandNoSuchException("Mode", "(none)", modes.keySet());
		final String name = args[0].toLowerCase();
		if (name.contains("*"))
		{
			if (args.length != 1)
				throw new CrazyCommandUsageException("<*>", "<Mode*>", "<Mode> [NewValue]");
			final Pattern pattern = Pattern.compile(StringUtils.replace(name, "*", ".*"));
			for (final Entry<String, Mode<?>> temp : modes.entrySet())
				if (pattern.matcher(temp.getKey()).matches())
					temp.getValue().showValue(player, squad);
			return;
		}
		final Mode<?> mode = modes.get(name);
		if (mode == null)
		{
			final TreeSet<String> alternatives = new TreeSet<String>();
			final Pattern pattern = Pattern.compile(".*" + name + ".*");
			for (final String temp : modes.keySet())
				if (pattern.matcher(temp).matches())
					alternatives.add(temp);
			throw new CrazyCommandNoSuchException("Mode", args[0], alternatives);
		}
		else if (args.length == 1)
			mode.showValue(player, squad);
		else
			try
			{
				mode.setValue(player, squad, ChatHelperExtended.shiftArray(args, 1));
			}
			catch (final CrazyCommandException e)
			{
				e.addCommandPrefix(args[0]);
				throw e;
			}
	}

	@Override
	public List<String> tab(final Player player, final Squad squad, final String[] args)
	{
		final List<String> res = new ArrayList<String>();
		if (args.length == 1)
		{
			final String last = args[args.length - 1].toLowerCase();
			for (final String mode : modes.keySet())
				if (mode.startsWith(last))
					res.add(mode);
		}
		else
		{
			final Mode<?> mode = modes.get(args[0].toLowerCase());
			if (mode != null)
				res.addAll(mode.tab(squad, ChatHelperExtended.shiftArray(args, 1)));
		}
		return res;
	}

	@Override
	public boolean hasAccessPermission(final Player player, final Squad squad)
	{
		return squad.getOwner() == player;
	}

	public void addMode(final Mode<?> mode)
	{
		modes.put(mode.getName().toLowerCase(), mode);
	}

	public abstract class Mode<S> implements Named
	{

		protected final String name;
		protected final Class<S> clazz;

		public Mode(final String name, final Class<S> clazz)
		{
			super();
			this.name = name;
			this.clazz = clazz;
		}

		@Override
		public final String getName()
		{
			return name;
		}

		public final Class<S> getClazz()
		{
			return clazz;
		}

		@Localized("CRAZYSQUADS.COMMAND.SQUAD.MODE.CHANGE $Name$ $Value$")
		public void showValue(final Player player, final Squad squad)
		{
			plugin.sendLocaleMessage("COMMAND.SQUAD.MODE.CHANGE", player, name, getValue(squad));
		}

		public abstract S getValue(Squad squad);

		public abstract void setValue(Player player, Squad squad, String... args) throws CrazyException;

		public abstract void setValue(Squad squad, S newValue) throws CrazyException;

		public List<String> tab(final Squad squad, final String... args)
		{
			return new ArrayList<String>();
		}
	}
}
