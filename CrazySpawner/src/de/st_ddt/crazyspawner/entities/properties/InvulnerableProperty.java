package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class InvulnerableProperty extends MetadataProperty
{

	public final static String METAHEADER = "INVULNERABLE";
	protected final boolean invulnerable;

	public InvulnerableProperty()
	{
		super();
		this.invulnerable = false;
	}

	public InvulnerableProperty(final ConfigurationSection config)
	{
		super(config);
		this.invulnerable = config.getBoolean("invulnerable", false);
	}

	public InvulnerableProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final BooleanParamitrisable invulnerableParam = (BooleanParamitrisable) params.get("invulnerable");
		this.invulnerable = invulnerableParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return true;
	}

	@Override
	public void apply(final Entity entity)
	{
		if (invulnerable)
			entity.setMetadata(METAHEADER, this);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final BooleanParamitrisable invulnerableParam = new BooleanParamitrisable(invulnerable);
		params.put("invulnerable", invulnerableParam);
		params.put("invincible", invulnerableParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "invulnerable", invulnerable);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "invulnerable", "boolean (false/true)");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.INVULNERABLE $Invulnerable$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.INVULNERABLE", target, invulnerable);
	}

	@Override
	public boolean equalsDefault()
	{
		return invulnerable == false;
	}
}
