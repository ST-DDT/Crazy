package de.st_ddt.crazylogin.commands;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;
import de.st_ddt.crazyplugin.commands.CrazyPlayerDataPluginCommandExecutorInterface;

public interface CommandExecutorInterface<S extends LoginPlugin<? extends LoginData>> extends CrazyPlayerDataPluginCommandExecutorInterface<LoginData, S>
{
}
