package de.st_ddt.crazycaptcha.captcha;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazycaptcha.commands.CrazyCaptchaCommandExecutor;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutorInterface;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandCircumstanceException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public abstract class AbstractCaptchaGenerator implements CaptchaGenerator
{

	protected final CrazyCaptcha plugin;

	public AbstractCaptchaGenerator(final CrazyCaptcha plugin)
	{
		super();
		this.plugin = plugin;
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "name", getName());
	}

	@Override
	public CrazyCommandExecutorInterface getCommands()
	{
		return new CrazyCaptchaCommandExecutor(CrazyCaptcha.getPlugin())
		{

			@Override
			public void command(final CommandSender sender, final String[] args) throws CrazyException
			{
				throw new CrazyCommandCircumstanceException("if captcha generator supports commands!");
			}
		};
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
