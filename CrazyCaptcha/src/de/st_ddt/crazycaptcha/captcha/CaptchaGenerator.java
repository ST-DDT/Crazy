package de.st_ddt.crazycaptcha.captcha;

import de.st_ddt.crazyplugin.commands.CrazyCommandExecutorInterface;
import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.Named;

public interface CaptchaGenerator extends ConfigurationSaveable, Named
{

	/**
	 * Used when loading a CaptchaGenerator from Config
	 * 
	 * @param plugin
	 *            This is CrazyCaptcha.
	 * @param config
	 *            The config section this generator is loaded from. (May be null)
	 */
	// public CaptchaGenerator( CrazyCaptcha plugin, ConfigurationSection config)
	// {
	// }
	/**
	 * Used when creating an CaptchaGenerator with Commands
	 * 
	 * @param plugin
	 *            This is CrazyCaptcha
	 * @param args
	 *            The parameters used to create this generator.
	 */
	// public CaptchaGenerator( CrazyCaptcha plugin, String[] args) throws CrazyException
	// {
	// }
	/**
	 * Generate a new captcha.
	 * 
	 * @return The generated captcha.
	 */
	public Captcha generateCaptcha();

	public CrazyCommandExecutorInterface getCommands();
}
