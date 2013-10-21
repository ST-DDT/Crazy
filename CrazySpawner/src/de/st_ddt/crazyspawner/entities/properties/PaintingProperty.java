package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Art;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class PaintingProperty extends BasicProperty
{

	protected final Art art;

	public PaintingProperty()
	{
		super();
		this.art = null;
	}

	public PaintingProperty(final Art art)
	{
		super();
		this.art = art;
	}

	public PaintingProperty(final ConfigurationSection config)
	{
		super(config);
		final String artName = config.getString("art", "default");
		if (artName.equals("default"))
			this.art = null;
		else
		{
			Art art = null;
			try
			{
				art = Art.valueOf(artName);
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s art " + artName + " was corrupted/invalid and has been removed!");
				art = null;
			}
			this.art = art;
		}
	}

	public PaintingProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		@SuppressWarnings("unchecked")
		final EnumParamitrisable<Art> artParam = (EnumParamitrisable<Art>) params.get("art");
		this.art = artParam.getValue();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Painting painting = (Painting) entity;
		if (art != null)
			painting.setArt(art, true);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final EnumParamitrisable<Art> facingParam = new EnumParamitrisable<>("Art", art, Art.values());
		params.put("a", facingParam);
		params.put("art", facingParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (art == null)
			config.set(path + "art", "default");
		else
			config.set(path + "art", art.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "art", "Art");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.PAINTING.ART $Art$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.PAINTING.ART", target, art == null ? "Default" : art.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return art == null;
	}
}
