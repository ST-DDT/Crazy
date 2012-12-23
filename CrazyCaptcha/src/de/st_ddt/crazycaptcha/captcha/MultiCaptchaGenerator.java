package de.st_ddt.crazycaptcha.captcha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.commands.CrazyCommandTreeExecutor;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandMainMode;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.locales.Localized;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;

public final class MultiCaptchaGenerator extends AbstractCaptchaGenerator
{

	private final Pattern PATTERN_SPACE = Pattern.compile(" ");
	private final Random random = new Random();
	private final CrazyCommandTreeExecutor<CrazyCaptcha> commands;
	private final Map<String, CaptchaGenerator> availableGenerators = new HashMap<String, CaptchaGenerator>(CaptchaHelper.getAvailableGenerators().size());
	private final List<CaptchaGenerator> activeGenerators = new ArrayList<CaptchaGenerator>(CaptchaHelper.getAvailableGenerators().size());

	public MultiCaptchaGenerator(final CrazyCaptcha plugin, final ConfigurationSection config)
	{
		this(plugin);
		final String[] dummyArgs = new String[0];
		if (config == null)
		{
			for (final String name : CaptchaHelper.getAvailableGenerators())
				if (!name.equals("multi"))
					try
					{
						availableGenerators.put(name.toLowerCase(), CaptchaHelper.getCaptchaGenerator(plugin, name, dummyArgs));
					}
					catch (final CrazyException e)
					{
						e.printStackTrace();
					}
			activeGenerators.addAll(availableGenerators.values());
		}
		else
		{
			for (final String name : CaptchaHelper.getAvailableGenerators())
				if (!name.equals("multi"))
				{
					final ConfigurationSection generatorConfig = config.getConfigurationSection(name);
					if (generatorConfig == null)
						try
						{
							availableGenerators.put(name.toLowerCase(), CaptchaHelper.getCaptchaGenerator(plugin, name, dummyArgs));
						}
						catch (final CrazyException e)
						{
							e.printStackTrace();
						}
					else
						availableGenerators.put(name.toLowerCase(), CaptchaHelper.getCaptchaGenerator(plugin, generatorConfig));
				}
			final List<String> actives = config.getStringList("active");
			if (actives != null)
				for (final String active : actives)
					activeGenerators.add(availableGenerators.get(active.toLowerCase()));
			while (activeGenerators.remove(null))
				;
			if (activeGenerators.size() == 0)
				activeGenerators.add(availableGenerators.get("basic"));
		}
		registerCommands();
	}

	public MultiCaptchaGenerator(final CrazyCaptcha plugin, final String[] args) throws CrazyException
	{
		this(plugin);
		final Map<String, StringParamitrisable> generatorArgs = new HashMap<String, StringParamitrisable>();
		for (final String name : CaptchaHelper.getAvailableGenerators())
			if (!name.equals("multi"))
				generatorArgs.put(name, new StringParamitrisable(null));
		final Map<String, Paramitrisable> params = new TreeMap<String, Paramitrisable>();
		params.putAll(generatorArgs);
		ChatHelperExtended.readParameters(args, params);
		final String[] dummyArgs = new String[0];
		for (final Entry<String, StringParamitrisable> entry : generatorArgs.entrySet())
		{
			final String name = entry.getKey();
			final String argRaw = entry.getValue().getValue();
			if (argRaw == null)
				try
				{
					availableGenerators.put(name.toLowerCase(), CaptchaHelper.getCaptchaGenerator(plugin, name, dummyArgs));
				}
				catch (final CrazyException e)
				{
					e.printStackTrace();
				}
			else
			{
				final CaptchaGenerator generator = CaptchaHelper.getCaptchaGenerator(plugin, name, PATTERN_SPACE.split(argRaw));
				activeGenerators.add(generator);
				availableGenerators.put(name.toLowerCase(), generator);
			}
		}
		if (activeGenerators.size() == 0)
			activeGenerators.add(availableGenerators.get("basic"));
		registerCommands();
	}

	protected MultiCaptchaGenerator(final CrazyCaptcha plugin)
	{
		super(plugin);
		this.commands = new CrazyCommandTreeExecutor<CrazyCaptcha>(plugin);
	}

	private void registerCommands()
	{
		final CrazyPluginCommandMainMode modeCommand = new CrazyPluginCommandMainMode(plugin);
		commands.addSubCommand(modeCommand, "mode");
		final CrazyCommandTreeExecutor<CrazyCaptcha> generators = new CrazyCommandTreeExecutor<CrazyCaptcha>(plugin);
		commands.addSubCommand(generators, "g", "gen", "generators");
		for (final Entry<String, CaptchaGenerator> entry : availableGenerators.entrySet())
			generators.addSubCommand(entry.getValue().getCommands(), entry.getKey());
		registerModes(modeCommand);
	}

	@Localized("CRAZYCAPTCHA.GENERATORMODE.CHANGE")
	private void registerModes(final CrazyPluginCommandMainMode modeCommand)
	{
		for (final CaptchaGenerator generator : availableGenerators.values())
			modeCommand.addMode(modeCommand.new BooleanTrueMode(generator.getName() + "Enabled")
			{

				@Override
				public void setValue(final Boolean newValue) throws CrazyException
				{
					if (newValue)
						activeGenerators.add(generator);
					else
						activeGenerators.remove(generator);
					plugin.saveConfiguration();
				}

				@Override
				public Boolean getValue()
				{
					return activeGenerators.contains(generator);
				}
			});
	}

	@Override
	public final String getName()
	{
		return "Multi";
	}

	@Override
	public Captcha generateCaptcha()
	{
		return activeGenerators.get(random.nextInt(activeGenerators.size())).generateCaptcha();
	}

	@Override
	public CrazyCommandTreeExecutor<CrazyCaptcha> getCommands()
	{
		return commands;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		super.save(config, path);
		final List<String> actives = new ArrayList<String>(activeGenerators.size());
		for (final CaptchaGenerator active : activeGenerators)
		{
			actives.add(active.getName());
			active.save(config, path + active.getName().toLowerCase() + ".");
		}
		config.set(path + "active", actives);
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
