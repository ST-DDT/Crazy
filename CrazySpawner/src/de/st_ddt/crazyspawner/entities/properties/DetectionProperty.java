package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.DetectionMeta;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class DetectionProperty extends MetadataProperty implements DetectionMeta
{

	protected final double detectionRange;

	public DetectionProperty()
	{
		super();
		this.detectionRange = -1;
	}

	public DetectionProperty(final ConfigurationSection config)
	{
		super(config);
		this.detectionRange = Math.max(config.getDouble("detectionRange", -1), -1);
	}

	public DetectionProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable detectionRangeParam = (DoubleParamitrisable) params.get("detectionrange");
		this.detectionRange = Math.max(detectionRangeParam.getValue(), -1);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Creature creature = (Creature) entity;
		if (detectionRange != -1)
			creature.setMetadata(DetectionMeta.METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable detectionRangeParam = new DoubleParamitrisable(detectionRange);
		params.put("dr", detectionRangeParam);
		params.put("detectionrange", detectionRangeParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "detectionRange", detectionRange);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "detectionRange", "double (-1=default)");
	}

	@Override
	public double getDetectionRange()
	{
		return detectionRange;
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.DETECTIONRANGE $DetectionRange$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.DETECTIONRANGE", target, detectionRange);
	}
}
