package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.CustomEntitySpawner.FallingBlockSpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MaterialParamitriable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class FallingBlockProperty extends FallingBlockSpawner implements EntityPropertyInterface
{

	protected final boolean dropItem;

	public FallingBlockProperty()
	{
		super();
		this.dropItem = false;
	}

	public FallingBlockProperty(final ConfigurationSection config)
	{
		super(Material.valueOf(config.getString("material", "STONE")), (byte) config.getInt("data", 0));
		this.dropItem = config.getBoolean("dropItem", false);
	}

	public FallingBlockProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(((MaterialParamitriable) params.get("material")).getValue(), ((IntegerParamitrisable) params.get("data")).getValue().byteValue());
		final BooleanParamitrisable dropItemParam = (BooleanParamitrisable) params.get("dropitem");
		this.dropItem = dropItemParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return FallingBlock.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final FallingBlock block = (FallingBlock) entity;
		block.setDropItem(dropItem);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final MaterialParamitriable materialParam = new MaterialParamitriable(material);
		params.put("m", materialParam);
		params.put("mat", materialParam);
		params.put("material", materialParam);
		final IntegerParamitrisable dataParamitrisable = new IntegerParamitrisable(data);
		params.put("data", dataParamitrisable);
		final BooleanParamitrisable dropItemParam = new BooleanParamitrisable(dropItem);
		params.put("di", dropItemParam);
		params.put("dropitem", dropItemParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "material", material.name());
		config.set(path + "data", data);
		config.set(path + "dropItem", dropItem);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "material", "Material (Blocks only)");
		config.set(path + "data", "byte");
		config.set(path + "dropItem", "boolean (true/false)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.MATERIAL $Material$ $Data$", "CRAZYSPAWNER.ENTITY.PROPERTY.DROPITEM $DropItem$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.MATERIAL", target, material.name(), data & 0xFF);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DROPITEM", target, dropItem);
	}

	@Override
	public boolean equalsDefault()
	{
		return dropItem == false;
	}
}
