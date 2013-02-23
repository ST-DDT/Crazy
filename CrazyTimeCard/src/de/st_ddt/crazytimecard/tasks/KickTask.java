package de.st_ddt.crazytimecard.tasks;

import org.bukkit.entity.Player;

import de.st_ddt.crazytimecard.CrazyTimeCard;
import de.st_ddt.crazytimecard.data.PlayerTimeData;
import de.st_ddt.crazyutil.source.Localized;

public class KickTask implements Runnable
{

	private final String name;

	public KickTask(final String name)
	{
		super();
		this.name = name;
	}

	public KickTask(final Player player)
	{
		this(player.getName());
	}

	@Override
	@Localized("CRAZYTIMECARD.KICKED.TIME.EXCEEDED")
	public void run()
	{
		final CrazyTimeCard plugin = CrazyTimeCard.getPlugin();
		final PlayerTimeData data = plugin.getPlayerData(name);
		if (data == null)
			return;
		if (data.isActive())
			return;
		final Player player = data.getPlayer();
		if (player == null)
			return;
		player.kickPlayer(plugin.getLocale().getLocaleMessage(player, "KICKED.TIME.EXCEEDED"));
	}
}
