package de.st_ddt.crazylogin.events;

import org.bukkit.entity.Player;

import de.st_ddt.crazylogin.LoginPlugin;
import de.st_ddt.crazylogin.data.LoginData;

public class CrazyLoginLoginEvent<S extends LoginData> extends CrazyLoginPlayerDataEvent<S>
{

	public CrazyLoginLoginEvent(final LoginPlugin<S> plugin, final Player player, final S data)
	{
		super(plugin, player, data);
	}
}
