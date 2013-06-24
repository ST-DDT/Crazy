package de.st_ddt.crazyutil.paramitrisable;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class PlayerItemStackParamitrisable extends ItemStackParamitrisable
{

	public static ItemStackParamitrisable getParamitrisableFor(final ItemStack defaultValue, final CommandSender sender)
	{
		if (sender instanceof Player)
			return new PlayerItemStackParamitrisable(defaultValue, (Player) sender);
		else
			return new ItemStackParamitrisable(defaultValue);
	}

	protected final Player player;

	public PlayerItemStackParamitrisable(final ItemStack defaultValue, final Player player)
	{
		super(defaultValue);
		if (player == null)
			throw new IllegalArgumentException("Player cannot be null!");
		this.player = player;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		if (parameter.equalsIgnoreCase("ItemInHand"))
			value = player.getItemInHand();
		else if (parameter.equalsIgnoreCase("Hand"))
			value = player.getItemInHand();
		else if (parameter.toLowerCase().startsWith("fast"))
		{
			if (parameter.equalsIgnoreCase("Fast1"))
				value = player.getInventory().getItem(0);
			else if (parameter.equalsIgnoreCase("Fast2"))
				value = player.getInventory().getItem(1);
			else if (parameter.equalsIgnoreCase("Fast3"))
				value = player.getInventory().getItem(2);
			else if (parameter.equalsIgnoreCase("Fast4"))
				value = player.getInventory().getItem(3);
			else if (parameter.equalsIgnoreCase("Fast5"))
				value = player.getInventory().getItem(4);
			else if (parameter.equalsIgnoreCase("Fast6"))
				value = player.getInventory().getItem(5);
			else if (parameter.equalsIgnoreCase("Fast7"))
				value = player.getInventory().getItem(6);
			else if (parameter.equalsIgnoreCase("Fast8"))
				value = player.getInventory().getItem(7);
			else if (parameter.equalsIgnoreCase("Fast9"))
				value = player.getInventory().getItem(8);
			else
				super.setParameter(parameter);
		}
		else if (parameter.toLowerCase().startsWith("armor"))
		{
			if (parameter.equalsIgnoreCase("ArmorBoots"))
				value = player.getInventory().getBoots();
			else if (parameter.equalsIgnoreCase("ArmorLeggings"))
				value = player.getInventory().getLeggings();
			else if (parameter.equalsIgnoreCase("ArmorChestplate"))
				value = player.getInventory().getChestplate();
			else if (parameter.equalsIgnoreCase("ArmorHelmet"))
				value = player.getInventory().getHelmet();
			else
				super.setParameter(parameter);
		}
		else if (parameter.equalsIgnoreCase("Boots"))
			value = player.getInventory().getBoots();
		else if (parameter.equalsIgnoreCase("Leggings"))
			value = player.getInventory().getLeggings();
		else if (parameter.equalsIgnoreCase("Chestplate"))
			value = player.getInventory().getChestplate();
		else if (parameter.equalsIgnoreCase("Helmet"))
			value = player.getInventory().getHelmet();
		else
			super.setParameter(parameter);
	}

	@Override
	public List<String> tab(String parameter)
	{
		final List<String> res = super.tab(parameter);
		parameter = parameter.toLowerCase();
		if ("iteminhand".startsWith(parameter))
			res.add("ItemInHand");
		if ("hand".startsWith(parameter))
			res.add("Hand");
		if ("fast1".startsWith(parameter))
			res.add("Fast1");
		if ("fast2".startsWith(parameter))
			res.add("Fast2");
		if ("fast3".startsWith(parameter))
			res.add("Fast3");
		if ("fast4".startsWith(parameter))
			res.add("Fast4");
		if ("fast5".startsWith(parameter))
			res.add("Fast5");
		if ("fast6".startsWith(parameter))
			res.add("Fast6");
		if ("fast7".startsWith(parameter))
			res.add("Fast7");
		if ("fast8".startsWith(parameter))
			res.add("Fast8");
		if ("fast9".startsWith(parameter))
			res.add("Fast9");
		if ("armorboots".startsWith(parameter))
			res.add("ArmorBoots");
		if ("armorleggings".startsWith(parameter))
			res.add("ArmorLeggings");
		if ("armorchestplate".startsWith(parameter))
			res.add("ArmorChestplate");
		if ("armorhelmet".startsWith(parameter))
			res.add("ArmorHelmet");
		if ("boots".startsWith(parameter))
			res.add("Boots");
		if ("leggings".startsWith(parameter))
			res.add("Leggings");
		if ("chestplate".startsWith(parameter))
			res.add("Chestplate");
		if ("helmet".startsWith(parameter))
			res.add("Helmet");
		return res;
	}
}
