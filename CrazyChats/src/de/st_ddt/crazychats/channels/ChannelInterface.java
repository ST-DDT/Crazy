package de.st_ddt.crazychats.channels;

import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

import de.st_ddt.crazyutil.Named;

public interface ChannelInterface extends Named
{

	public List<String> getAliases();

	public boolean hasTalkPermission(Player player);

	public Set<Player> getTargets(Player player);

	public String getFormat();
}
