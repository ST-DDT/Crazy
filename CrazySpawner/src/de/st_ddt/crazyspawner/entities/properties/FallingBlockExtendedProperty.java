package de.st_ddt.crazyspawner.entities.properties;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.FallingBlockMeta;
import de.st_ddt.crazyutil.ObjectSaveLoadHelper;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MaterialParamitriable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.PlayerItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class FallingBlockExtendedProperty extends MetadataProperty implements FallingBlockMeta
{

	protected final static int MAXCONTENTCOUNT = 2 * 3 * 9;
	protected final boolean despawn;
	protected final Material placeMaterial;
	protected final Byte placeData;
	protected final Map<Integer, ItemStack> content;

	public FallingBlockExtendedProperty()
	{
		super();
		this.despawn = false;
		this.placeMaterial = null;
		this.placeData = null;
		this.content = new HashMap<Integer, ItemStack>();
	}

	public FallingBlockExtendedProperty(final ConfigurationSection config)
	{
		super(config);
		this.despawn = config.getBoolean("despawnOnImpact", false);
		final String placeMaterialName = config.getString("placeMaterial", null);
		if (placeMaterialName == null)
			this.placeMaterial = null;
		else
		{
			Material placeMaterial = null;
			try
			{
				placeMaterial = Material.valueOf(placeMaterialName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s color " + placeMaterialName + " was corrupted/invalid and has been removed!");
			}
			this.placeMaterial = placeMaterial;
		}
		if (config.contains("placeData"))
			this.placeData = (byte) config.getInt("placeData");
		else
			this.placeData = null;
		final ConfigurationSection inventoryConfig = config.getConfigurationSection("content");
		if (inventoryConfig == null)
			this.content = new HashMap<Integer, ItemStack>();
		else
			this.content = ObjectSaveLoadHelper.loadInventory(config);
	}

	public FallingBlockExtendedProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable despawnParam = (BooleanParamitrisable) params.get("despawnonimpact");
		this.despawn = despawnParam.getValue();
		final MaterialParamitriable placeMaterialParam = (MaterialParamitriable) params.get("placematerial");
		this.placeMaterial = placeMaterialParam.getValue();
		final IntegerParamitrisable placeDataParam = (IntegerParamitrisable) params.get("placedata");
		final Integer placeData = placeDataParam.getValue();
		this.placeData = placeData == null ? null : placeData.byteValue();
		this.content = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < MAXCONTENTCOUNT; i++)
		{
			final ItemStackParamitrisable contentParam = (ItemStackParamitrisable) params.get("content" + i);
			final ItemStack item = contentParam.getValue();
			if (item != null)
				content.put(i, item);
		}
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return FallingBlock.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (!equalsDefault())
			entity.setMetadata(METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable despawnParam = new BooleanParamitrisable(despawn);
		params.put("doi", despawnParam);
		params.put("donimpact", despawnParam);
		params.put("despawnonimpact", despawnParam);
		final MaterialParamitriable placeMaterialParam = new MaterialParamitriable(placeMaterial);
		params.put("pm", placeMaterialParam);
		params.put("pmat", placeMaterialParam);
		params.put("pmaterial", placeMaterialParam);
		params.put("placemat", placeMaterialParam);
		params.put("placematerial", placeMaterialParam);
		final IntegerParamitrisable placeDataParam = new IntegerParamitrisable(placeData == null ? null : new Integer(placeData));
		params.put("pd", placeDataParam);
		params.put("pdata", placeDataParam);
		params.put("placed", placeDataParam);
		params.put("placedata", placeDataParam);
		for (int i = 0; i < MAXCONTENTCOUNT; i++)
		{
			final ItemStackParamitrisable contentParam = PlayerItemStackParamitrisable.getParamitrisableFor(content.get(i), sender);
			params.put("c" + i, contentParam);
			params.put("content" + i, contentParam);
		}
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "despawnOnImpact", despawn);
		config.set(path + "placematerial", placeMaterial);
		config.set(path + "placedata", placeData);
		ObjectSaveLoadHelper.saveInventory(config, path + "content.", content);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "despawnOnImpact", "boolean (true/false)");
		config.set(path + "placeMaterial", "Material (Blocks only)");
		config.set(path + "placeData", "byte");
		config.set(path + "content.0", "Item");
		config.set(path + "content.1", "Item");
		config.set(path + "content." + MAXCONTENTCOUNT, "Item");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.DESPAWNONIMPACT $Despawn$", "CRAZYSPAWNER.ENTITY.PROPERTY.PLACEMATERIAL $Material$ $Data$", "CRAZYSPAWNER.ENTITY.PROPERTY.CONTENT $Index$ $Item$" })
	public void show(final CommandSender target)
	{
		final CrazySpawner plugin = CrazySpawner.getPlugin();
		plugin.sendLocaleMessage("ENTITY.PROPERTY.DESPAWNONIMPACT", target, despawn);
		plugin.sendLocaleMessage("ENTITY.PROPERTY.PLACEMATERIAL", target, placeMaterial == null ? "Default" : placeMaterial.name(), placeData == null ? "Default" : placeData & 0xFF);
		for (int i = 0; i < MAXCONTENTCOUNT; i++)
		{
			final ItemStack item = content.get(i);
			if (item != null)
				plugin.sendLocaleMessage("ENTITY.PROPERTY.CONTENT", target, i, item);
		}
	}

	@Override
	public boolean equalsDefault()
	{
		return despawn == false && placeMaterial == null && placeData == null && content.isEmpty();
	}

	@Override
	public boolean isDespawningOnImpactEnabled()
	{
		return despawn;
	}

	@Override
	public Material getPlacedMaterial()
	{
		return placeMaterial;
	}

	@Override
	public Byte getPlacedMaterialData()
	{
		return placeData;
	}

	@Override
	public void apply(final Block block)
	{
		final BlockState state = block.getState();
		if (state instanceof InventoryHolder)
		{
			final InventoryHolder holder = (InventoryHolder) state;
			final Inventory inventory = holder.getInventory();
			for (int i = 0; i < Math.min(inventory.getSize(), content.size()); i++)
			{
				final ItemStack item = content.get(i);
				if (item != null)
					inventory.setItem(i, item.clone());
			}
		}
	}
}
