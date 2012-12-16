package de.st_ddt.crazyenchanter;

import org.bukkit.enchantments.Enchantment;

import de.st_ddt.crazyenchanter.commands.CrazyEnchanterPlayerCommandEnchant;
import de.st_ddt.crazyplugin.CrazyPlugin;

public class CrazyEnchanter extends CrazyPlugin
{

	private static CrazyEnchanter plugin;

	public static CrazyEnchanter getPlugin()
	{
		return plugin;
	}

	private void registerCommands()
	{
		getCommand("enchant").setExecutor(new CrazyEnchanterPlayerCommandEnchant(this));
	}

	@Override
	public void onLoad()
	{
		plugin = this;
		super.onLoad();
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		registerCommands();
	}

	public String[] getEnchantmentNames()
	{
		final Enchantment[] enchantments = Enchantment.values();
		final int length = enchantments.length;
		final String[] res = new String[length];
		for (int i = 0; i < length; i++)
			res[i] = enchantments[i].getName();
		return res;
	}
}
