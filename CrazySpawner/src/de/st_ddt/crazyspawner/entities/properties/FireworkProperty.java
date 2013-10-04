package de.st_ddt.crazyspawner.entities.properties;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyspawner.CrazySpawner;
import de.st_ddt.crazyutil.paramitrisable.BooleanParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.IntegerParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.ItemStackParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.MultiParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;
import de.st_ddt.crazyutil.paramitrisable.RGBColorParamitrisable;
import de.st_ddt.crazyutil.paramitrisable.TabbedParamitrisable;
import de.st_ddt.crazyutil.source.Localized;

public final class FireworkProperty extends BasicProperty
{

	protected final FireworkMeta meta;

	public FireworkProperty()
	{
		super();
		this.meta = null;
	}

	public FireworkProperty(final ConfigurationSection config)
	{
		super(config);
		final Object object = config.get("firework", null);
		if (object == null || !(object instanceof FireworkMeta))
			this.meta = null;
		else
			this.meta = (FireworkMeta) object;
	}

	@SuppressWarnings("unchecked")
	public FireworkProperty(final Map<String, ? extends Paramitrisable> params)
	{
		super(params);
		final ItemStackParamitrisable itemParam = (ItemStackParamitrisable) params.get("item");
		final ItemStack item = itemParam.getValue();
		if (item == null || item.getItemMeta() == null || !(item.getItemMeta() instanceof FireworkMeta))
		{
			final FireworkMeta meta = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK);
			final IntegerParamitrisable powerParam = (IntegerParamitrisable) params.get("power");
			meta.setPower(Math.min(Math.max(powerParam.getValue(), 0), 127));
			int count = 0;
			while (true)
			{
				count++;
				final BooleanParamitrisable removeParam = (BooleanParamitrisable) params.get("removefirework" + count);
				if (removeParam == null)
					break;
				if (removeParam.getValue())
					continue;
				final Builder builder = FireworkEffect.builder();
				final BooleanParamitrisable flickerParam = (BooleanParamitrisable) params.get("flicker" + count);
				builder.flicker(flickerParam.getValue());
				final BooleanParamitrisable trailParam = (BooleanParamitrisable) params.get("trail" + count);
				builder.trail(trailParam.getValue());
				final MultiParamitrisable<Color> colorParam = (MultiParamitrisable<Color>) params.get("color" + count);
				builder.withColor(colorParam.getValue());
				final MultiParamitrisable<Color> fadeParam = (MultiParamitrisable<Color>) params.get("fade" + count);
				builder.withFade(fadeParam.getValue());
				meta.addEffect(builder.build());
			}
			this.meta = meta;
		}
		else
			this.meta = (FireworkMeta) item.getItemMeta();
	}

	@Override
	public void apply(final Entity entity)
	{
		final Firework firework = (Firework) entity;
		if (meta != null)
			firework.setFireworkMeta(meta);
	}

	@Override
	public void getCommandParams(final Map<String, ? super TabbedParamitrisable> params, final CommandSender sender)
	{
		final ItemStackParamitrisable itemParam = new ItemStackParamitrisable(null);
		params.put("item", itemParam);
		params.put("copyfromitem", itemParam);
		IntegerParamitrisable powerParam;
		int count = 0;
		if (meta == null)
			powerParam = new IntegerParamitrisable(2);
		else
		{
			powerParam = new IntegerParamitrisable(meta.getPower());
			for (final FireworkEffect effect : meta.getEffects())
			{
				count++;
				final BooleanParamitrisable removeParam = new BooleanParamitrisable(false);
				params.put("rmfw" + count, removeParam);
				params.put("rmfirework" + count, removeParam);
				params.put("removefw" + count, removeParam);
				params.put("removefirework" + count, removeParam);
				final BooleanParamitrisable flickerParam = new BooleanParamitrisable(effect.hasFlicker())
				{

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						super.setParameter(parameter);
						removeParam.setValue(false);
					}
				};
				params.put("f" + count, flickerParam);
				params.put("flicker" + count, flickerParam);
				final BooleanParamitrisable trailParam = new BooleanParamitrisable(effect.hasTrail())
				{

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						super.setParameter(parameter);
						removeParam.setValue(false);
					}
				};
				params.put("t" + count, trailParam);
				params.put("trail" + count, trailParam);
				final MultiParamitrisable<Color> colorParam = new MultiParamitrisable<Color>(new RGBColorParamitrisable(null))
				{

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						super.setParameter(parameter);
						removeParam.setValue(false);
					}
				};
				params.put("c" + count, colorParam);
				params.put("color" + count, colorParam);
				final MultiParamitrisable<Color> fadeParam = new MultiParamitrisable<Color>(new RGBColorParamitrisable(null))
				{

					@Override
					public void setParameter(final String parameter) throws CrazyException
					{
						super.setParameter(parameter);
						removeParam.setValue(false);
					}
				};
				params.put("fc" + count, fadeParam);
				params.put("fade" + count, fadeParam);
				params.put("fadec" + count, fadeParam);
				params.put("fadecolor" + count, fadeParam);
			}
		}
		params.put("p", powerParam);
		params.put("power", powerParam);
		count++;
		final BooleanParamitrisable removeParam = new BooleanParamitrisable(true);
		params.put("rmfw" + count, removeParam);
		params.put("rmfirework" + count, removeParam);
		params.put("removefw" + count, removeParam);
		params.put("removefirework" + count, removeParam);
		final BooleanParamitrisable flickerParam = new BooleanParamitrisable(false)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				removeParam.setValue(false);
			}
		};
		params.put("f" + count, flickerParam);
		params.put("flicker" + count, flickerParam);
		final BooleanParamitrisable trailParam = new BooleanParamitrisable(false)
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				removeParam.setValue(false);
			}
		};
		params.put("t" + count, trailParam);
		params.put("trail" + count, trailParam);
		final MultiParamitrisable<Color> colorParam = new MultiParamitrisable<Color>(new RGBColorParamitrisable(null))
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				removeParam.setValue(false);
			}
		};
		params.put("c" + count, colorParam);
		params.put("color" + count, colorParam);
		final MultiParamitrisable<Color> fadeParam = new MultiParamitrisable<Color>(new RGBColorParamitrisable(null))
		{

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				super.setParameter(parameter);
				removeParam.setValue(false);
			}
		};
		params.put("fc" + count, fadeParam);
		params.put("fade" + count, fadeParam);
		params.put("fadec" + count, fadeParam);
		params.put("fadecolor" + count, fadeParam);
	}

	@Override
	public void save(final ConfigurationSection config, final String path)
	{
		config.set(path + "firework", meta);
	}

	@Override
	public void dummySave(final ConfigurationSection config, final String path)
	{
		config.set(path + "firework", "FireWorkMeta");
	}

	@Override
	@Localized("CRAZYSPAWNER.ENTITY.PROPERTY.FIREWORK $Color$")
	public void show(final CommandSender target)
	{
		CrazySpawner.getPlugin().sendLocaleMessage("ENTITY.PROPERTY.FIREWORK", target, meta == null ? "Default" : meta);
	}

	@Override
	public boolean equalsDefault()
	{
		return meta == null;
	}
}
