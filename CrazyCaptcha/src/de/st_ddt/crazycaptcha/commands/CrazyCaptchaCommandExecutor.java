package de.st_ddt.crazycaptcha.commands;

import de.st_ddt.crazycaptcha.CrazyCaptcha;
import de.st_ddt.crazyplugin.commands.CrazyCommandExecutor;

public abstract class CrazyCaptchaCommandExecutor extends CrazyCommandExecutor<CrazyCaptcha>
{

	public CrazyCaptchaCommandExecutor(final CrazyCaptcha plugin)
	{
		super(plugin);
	}
}
