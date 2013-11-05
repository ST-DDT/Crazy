package de.st_ddt.crazyspawner.entities.properties;

import java.util.Arrays;
import java.util.Map;

import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;

import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.EnumParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public class HangingProperty extends BasicProperty
{

	protected final BlockFace face;

	public HangingProperty()
	{
		super();
		this.face = null;
	}

	public HangingProperty(final BlockFace face)
	{
		super();
		this.face = face;
		if (!Arrays.asList(getSupportedBlockFaces()).contains(face))
			throw new IllegalArgumentException("This block face is not supported!");
	}

	public HangingProperty(final ConfigurationSection config)
	{
		super(config);
		final String faceName = config.getString("facingDirection", "default");
		if (faceName.equals("default"))
			this.face = null;
		else
		{
			BlockFace face = null;
			try
			{
				face = BlockFace.valueOf(faceName);
				if (!Arrays.asList(getSupportedBlockFaces()).contains(face))
					throw new IllegalArgumentException("This block face is not supported!");
			}
			catch (final Exception e)
			{
				System.err.println(config.getName() + "'s facing direction " + faceName + " was corrupted/invalid and has been removed!");
				face = null;
			}
			this.face = face;
		}
	}

	public HangingProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		@SuppressWarnings("unchecked")
		final EnumParamitrisable<BlockFace> faceParam = (EnumParamitrisable<BlockFace>) params.get("facingdirection");
		this.face = faceParam.getValue();
	}

	@Override
	public boolean isApplicable(final Class<? extends Entity> clazz)
	{
		return Hanging.class.isAssignableFrom(clazz);
	}

	@Override
	public void apply(final Entity entity)
	{
		final Hanging hanging = (Hanging) entity;
		if (face != null)
			hanging.setFacingDirection(face, true);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final EnumParamitrisable<BlockFace> facingParam = new EnumParamitrisable<>("BlockFace", face, getSupportedBlockFaces());
		params.put("f", facingParam);
		params.put("face", facingParam);
		params.put("facing", facingParam);
		params.put("facingdirection", facingParam);
	}

	protected BlockFace[] getSupportedBlockFaces()
	{
		return new BlockFace[] { BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST };
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		if (face == null)
			config.set(path + "facingDirection", "default");
		else
			config.set(path + "facingDirection", face.name());
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "facingDirection", "BlockFace");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.HANGING.FACINGDIRECTION $Direction$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.HANGING.FACINGDIRECTION", target, face == null ? "Default" : face.name());
	}

	@Override
	public boolean equalsDefault()
	{
		return face == null;
	}
}
