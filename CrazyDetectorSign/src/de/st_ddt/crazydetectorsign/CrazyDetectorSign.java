package de.st_ddt.crazydetectorsign;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;

import de.st_ddt.crazydetectorsign.actions.Action;
import de.st_ddt.crazydetectorsign.actions.TimeOfDayRunnable;
import de.st_ddt.crazydetectorsign.actions.TimedAction;
import de.st_ddt.crazyplugin.CrazyPlugin;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;

public class CrazyDetectorSign extends CrazyPlugin
{

	protected CrazyDetectorSignBlockListener blockListener;
	protected static CrazyDetectorSign plugin;
	protected final HashMap<Location, Action> signs = new HashMap<Location, Action>();

	public static CrazyDetectorSign getPlugin()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		registerHooks();
		super.onEnable();
	}

	public void registerHooks()
	{
		this.blockListener = new CrazyDetectorSignBlockListener(this);
		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(blockListener, this);
	}

	@Override
	public void saveConfiguration()
	{
		int anz = 0;
		ConfigurationSection config = getConfig();
		config.set("signs", null);
		config = config.createSection("signs");
		for (Location location : signs.keySet())
		{
			anz++;
			ObjectSaveLoadHelper.saveLocation(config, "loc" + anz + ".", location, true, false);
		}
		super.saveConfiguration();
	}

	@Override
	public void loadConfiguration()
	{
		ConfigurationSection config = getConfig();
		config = config.getConfigurationSection("signs");
		if (config != null)
			for (String key : config.getKeys(false))
				registerSign(ObjectSaveLoadHelper.loadLocation(config.getConfigurationSection(key), null).getBlock());
	}

	private void registerSign(Block block)
	{
		if (block.getType() != Material.WALL_SIGN)
			return;
		registerSign(block, ((Sign) block.getState()).getLines());
	}

	public void registerSign(Block block, String[] lines)
	{
		if (block.getType() != Material.WALL_SIGN)
			return;
		Sign sign = (Sign) block.getState();
		Location attached = block.getLocation();
		World world = block.getWorld();
		switch (sign.getData().getData())
		{
			case 2:
				attached.add(0, 1, 1);
				break;
			case 3:
				attached.add(0, 1, -1);
				break;
			case 4:
				attached.add(1, 1, 0);
				break;
			case 5:
				attached.add(-1, 1, 0);
				break;
		}
		if (!lines[0].equals("[CD]"))
			return;
		if (lines[1].equalsIgnoreCase("timeofday"))
			signs.put(block.getLocation(), new TimedAction(Integer.parseInt(lines[3]), new TimeOfDayRunnable(attached, world, lines[2])));
	}

	public void unregisterSign(Location location)
	{
		Action action = signs.remove(location);
		save();
		if (action == null)
			return;
		action.disable();
	}

	@Override
	protected boolean isSupportingLanguages()
	{
		return false;
	}
}
