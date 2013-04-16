package de.st_ddt.crazyutil.paramitrisable;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class MaterialParamitriable extends TypedParamitrisable<Material>
{

	public MaterialParamitriable(final Material defaultValue)
	{
		super(defaultValue);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		value = Material.getMaterial(parameter.toUpperCase());
		if (value == null)
			try
			{
				value = Material.getMaterial(Integer.parseInt(parameter));
			}
			catch (final NumberFormatException e)
			{
				value = null;
			}
		if (value == null)
			throw new CrazyCommandNoSuchException("Material", parameter, tabHelp(parameter));
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return tabHelp(parameter);
	}

	public static List<String> tabHelp(String parameter)
	{
		final List<String> res = new LinkedList<String>();
		parameter = parameter.toUpperCase();
		for (final Material mat : Material.values())
			if (mat.toString().startsWith(parameter))
				res.add(mat.name());
		return res;
	}
}
