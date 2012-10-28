package de.st_ddt.crazyplugin.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.CrazyPluginInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandParameterException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandUsageException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.Named;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;

public class CrazyPluginCommandMainMode extends CrazyCommandExecutor<CrazyPluginInterface>
{

	protected final Map<String, Mode<?>> modes = new TreeMap<String, Mode<?>>();

	public CrazyPluginCommandMainMode(final CrazyPluginInterface plugin)
	{
		super(plugin);
	}

	@Override
	public void command(final CommandSender sender, final String[] args) throws CrazyException
	{
		if (!PermissionModule.hasPermission(sender, plugin.getName().toLowerCase() + ".mode"))
			throw new CrazyCommandPermissionException();
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
					temp.getValue().showValue(sender);
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
			mode.showValue(sender);
		else
			try
			{
				mode.setValue(sender, ChatHelperExtended.shiftArray(args, 1));
			}
			catch (final CrazyCommandException e)
			{
				e.addCommandPrefix(args[0]);
				throw e;
			}
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

		@Localized("CRAZYPLUGIN.MODE.CHANGE $Name$ $Value$")
		public void showValue(final CommandSender sender)
		{
			plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue());
		}

		public abstract S getValue();

		public abstract void setValue(CommandSender sender, String... args) throws CrazyException;

		public abstract void setValue(S newValue) throws CrazyException;
	}

	public abstract class IntegerMode extends Mode<Integer>
	{

		public IntegerMode(final String name)
		{
			super(name, Integer.class);
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			if (args.length > 1)
				throw new CrazyCommandUsageException("[Number (Integer)]");
			try
			{
				setValue(Integer.parseInt(args[0]));
				showValue(sender);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Integer)");
			}
		}
	}

	public abstract class LongMode extends Mode<Long>
	{

		public LongMode(final String name)
		{
			super(name, Long.class);
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			if (args.length > 1)
				throw new CrazyCommandUsageException("[Number (Long)]");
			try
			{
				setValue(Long.parseLong(args[0]));
				showValue(sender);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Long)");
			}
		}
	}

	public abstract class DoubleMode extends Mode<Double>
	{

		public DoubleMode(final String name)
		{
			super(name, Double.class);
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			if (args.length > 1)
				throw new CrazyCommandUsageException("[Number (Double)]");
			try
			{
				setValue(Double.parseDouble(args[0]));
				showValue(sender);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Number (Double)");
			}
		}
	}

	public abstract class BooleanMode extends Mode<Boolean>
	{

		public BooleanMode(final String name)
		{
			super(name, Boolean.class);
		}

		@Override
		public void showValue(final CommandSender sender)
		{
			plugin.sendLocaleMessage("MODE.CHANGE", sender, name, getValue() ? "True" : "False");
		}
	}

	public abstract class BooleanTrueMode extends BooleanMode
	{

		public BooleanTrueMode(final String name)
		{
			super(name);
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			if (args.length > 1)
				throw new CrazyCommandUsageException("[Boolean (true/false)]");
			try
			{
				final String value = args[0].toLowerCase();
				if (value.equals("false"))
					setValue(false);
				else if (value.equals("0"))
					setValue(false);
				else if (value.equals("n"))
					setValue(false);
				else if (value.equals("no"))
					setValue(false);
				else if (value.equals("off"))
					setValue(false);
				else
					setValue(true);
				showValue(sender);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Boolean (true/false)");
			}
		}
	}

	public abstract class BooleanFalseMode extends BooleanMode
	{

		public BooleanFalseMode(final String name)
		{
			super(name);
		}

		@Override
		public void setValue(final CommandSender sender, final String... args) throws CrazyException
		{
			if (args.length > 1)
				throw new CrazyCommandUsageException("[Boolean (true/false)]");
			try
			{
				final String value = args[0].toLowerCase();
				if (value.equals("true"))
					setValue(true);
				else if (value.equals("1"))
					setValue(true);
				else if (value.equals("y"))
					setValue(true);
				else if (value.equals("yes"))
					setValue(true);
				else if (value.equals("on"))
					setValue(true);
				else
					setValue(false);
				showValue(sender);
			}
			catch (final NumberFormatException e)
			{
				throw new CrazyCommandParameterException(0, "Boolean (true/false)");
			}
		}
	}

	@Override
	public List<String> tab(CommandSender sender, String[] args)
	{
		final String last = args[args.length - 1];
		final List<String> res = new ArrayList<String>();
		for (String mode : modes.keySet())
			if (mode.startsWith(last))
				res.add(mode + ":");
		return res;
	}
}
