package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.MaterialParamitriable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class EndermanProperty extends BasicProperty
{

	protected final Material material;

	public EndermanProperty()
	{
		super();
		this.material = null;
	}

	public EndermanProperty(final ConfigurationSection config)
	{
		super(config);
		final String materialName = config.getString("carriedMaterial");
		if (materialName == null)
			this.material = null;
		else
		{
			Material material = null;
			try
			{
				material = Material.valueOf(materialName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s material " + materialName + " was corrupted/invalid and has been removed!");
			}
			this.material = material;
		}
	}

	public EndermanProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final MaterialParamitriable materialParam = (MaterialParamitriable) params.get("carriedmaterial");
		this.material = materialParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Enderman enderman = (Enderman) entity;
		if (material != null)
			enderman.setCarriedMaterial(new MaterialData(material));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final MaterialParamitriable materialParam = new MaterialParamitriable(material);
		params.put("m", materialParam);
		params.put("mat", materialParam);
		params.put("material", materialParam);
		params.put("carriedmaterial", materialParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (material != null)
			config.set(path + "carriedMaterial", material.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "carriedMaterial", "Material");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.CARRIEDMATERIAL $Material$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.CARRIEDMATERIAL", target, material == null ? "None" : material.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return material == null;
	}
}
