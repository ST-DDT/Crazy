package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyspawner.entities.meta.NameMeta;
import de.st_ddt.crazyspawner.entities.meta.NameMeta.FixedNameMeta;
import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ColoredStringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.StringParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class NameProperty extends BasicProperty
{

	protected final String customName;
	protected final boolean showNameAboveHead;
	protected final boolean showHealth;

	public NameProperty()
	{
		super();
		this.customName = null;
		this.showNameAboveHead = false;
		this.showHealth = false;
	}

	public NameProperty(final ConfigurationSection config)
	{
		super(config);
		this.customName = config.getString("custonName", null);
		this.showNameAboveHead = config.getBoolean("showNameAboveHead", false);
		this.showHealth = config.getBoolean("showHealth", false);
	}

	public NameProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final StringParamitrisable customNameParam = (StringParamitrisable) params.get("customname");
		this.customName = customNameParam.getValue();
		final BooleanParamitrisable showNameAboveHeadParam = (BooleanParamitrisable) params.get("shownameabovehead");
		this.showNameAboveHead = showNameAboveHeadParam.getValue();
		final BooleanParamitrisable showHealthParam = (BooleanParamitrisable) params.get("showhealth");
		this.showHealth = showHealthParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final LivingEntity living = (LivingEntity) entity;
		if (customName != null)
			living.setCustomName(customName);
		if (showNameAboveHead)
			living.setCustomNameVisible(true);
		if (showHealth)
			living.setMetadata(NameMeta.METAHEADER, new FixedNameMeta(customName == null ? entity.getType().getName() : customName));
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final StringParamitrisable customNameParam = new ColoredStringParamitrisable(customName);
		params.put("cn", customNameParam);
		params.put("cname", customNameParam);
		params.put("customname", customNameParam);
		final BooleanParamitrisable showNameAboveHeadParam = new BooleanParamitrisable(showNameAboveHead);
		params.put("sn", showNameAboveHeadParam);
		params.put("sname", showNameAboveHeadParam);
		params.put("showname", showNameAboveHeadParam);
		params.put("shownameabovehead", showNameAboveHeadParam);
		final BooleanParamitrisable showHealthParam = new BooleanParamitrisable(showHealth);
		params.put("sh", showHealthParam);
		params.put("shealth", showHealthParam);
		params.put("showhealth", showHealthParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "customName", ChatHelper.decolorise(customName));
		config.set(path + "showNameAboveHead", showNameAboveHead);
		config.set(path + "showHealth", showHealth);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "customName", "Colored String");
		config.set(path + "showNameAboveHead", "boolean (true/false)");
		config.set(path + "showHealth", "boolean (true/false)");
	}

	@Override
	@Localized({ "CRAZYSPAWNER.ENTITY.PROPERTY.CUSTOMNAME $CustomName$", "CRAZYSPAWNER.ENTITY.PROPERTY.SHOWNAMEABOVEHEAD $ShowNameAboveHead$", "CRAZYSPAWNER.ENTITY.PROPERTY.SHOWHEALTH $ShowHealth$" })
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.CUSTOMNAME", target, customName == null ? "None" : customName);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SHOWNAMEABOVEHEAD", target, showNameAboveHead);
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.SHOWHEALTH", target, showHealth);
	}
}
