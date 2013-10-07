package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.AlarmMeta;
import de.st_ddt.crazyutil.paramitrisable.DoubleParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class AlarmProperty extends MetadataProperty implements AlarmMeta
{

	protected final double alarmRange;

	public AlarmProperty()
	{
		super();
		this.alarmRange = -1;
	}

	public AlarmProperty(final double alarmRange)
	{
		super();
		this.alarmRange = alarmRange;
	}

	public AlarmProperty(final ConfigurationSection config)
	{
		super(config);
		this.alarmRange = Math.max(config.getDouble("alarmRange", -1), -1);
	}

	public AlarmProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final DoubleParamitrisable alarmRangeParam = (DoubleParamitrisable) params.get("alarmrange");
		this.alarmRange = Math.max(alarmRangeParam.getValue(), -1);
	}

	@Override
	public void apply(final Entity entity)
	{
		if (alarmRange != -1)
			entity.setMetadata(AlarmMeta.METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final DoubleParamitrisable alarmRangeParam = new DoubleParamitrisable(alarmRange);
		params.put("ar", alarmRangeParam);
		params.put("alarmrange", alarmRangeParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "alarmRange", alarmRange);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "alarmRange", "int (-1=default)");
	}

	@Override
	public double getAlarmRange()
	{
		return alarmRange;
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.ALARMRANGE $AlarmRange$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.ALARMRANGE", target, alarmRange == -1 ? "Default" : alarmRange);
	}

	@Override
	public boolean equalsDefault()
	{
		return alarmRange == -1;
	}
}
