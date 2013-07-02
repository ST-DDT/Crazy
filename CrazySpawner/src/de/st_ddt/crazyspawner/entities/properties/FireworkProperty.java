package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class FireworkProperty extends BasicProperty
{

	protected final FireworkMeta meta;

	public FireworkProperty()
	{
		super();
		this.meta = null;
	}

	public FireworkProperty(final ConfigurationSection config)
	{
		super(config);
		final Object object = config.get("firework", null);
		if (object == null || !(object instanceof FireworkMeta))
			this.meta = null;
		else
			this.meta = (FireworkMeta) object;
	}

	public FireworkProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final ItemStackParamitrisable metaParam = (ItemStackParamitrisable) params.get("firework");
		final ItemStack item = metaParam.getValue();
		if (item == null || item.getType() != Material.FIREWORK)
			this.meta = null;
		else
			this.meta = (FireworkMeta) item.getItemMeta();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Firework firework = (Firework) entity;
		if (meta != null)
			firework.setFireworkMeta(meta);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final ItemStack item = new ItemStack(Material.FIREWORK);
		if (meta != null)
			item.setItemMeta(meta);
		final ItemStackParamitrisable metaParam = PlayerItemStackParamitrisable.getParamitrisableFor(item, sender);
		params.put("fw", metaParam);
		params.put("firework", metaParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "firework", meta);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "firework", "FireWorkMeta");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.FIREWORK $Color$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.FIREWORK", target, meta);
	}
}
