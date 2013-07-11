package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.PeacefulMeta;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class PeacefulProperty extends BasicProperty
{

	protected final boolean peaceful;

	public PeacefulProperty()
	{
		super();
		this.peaceful = false;
	}

	public PeacefulProperty(final ConfigurationSection config)
	{
		super(config);
		this.peaceful = config.getBoolean("peaceful", false);
	}

	public PeacefulProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable peacefulParam = (BooleanParamitrisable) params.get("peaceful");
		this.peaceful = peacefulParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		entity.setMetadata(PeacefulMeta.METAHEADER, PeacefulMeta.INSTANCE);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable peacefulParam = new BooleanParamitrisable(peaceful);
		params.put("peaceful", peacefulParam);
		params.put("forcepeaceful", peacefulParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "peaceful", peaceful);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "peaceful", "boolean (true/false)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.PEACEFUL $Peaceful$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PEACEFUL", target, peaceful);
	}

	@Override
	public boolean equalsDefault()
	{
		return peaceful == false;
	}
}
