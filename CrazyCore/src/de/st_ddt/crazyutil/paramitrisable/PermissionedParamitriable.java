package de.st_ddt.crazyutil.paramitrisable;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandPermissionException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.Named;

public class PermissionedParamitriable<S> extends TypedParamitrisable<S>
{

	protected final CommandSender sender;
	protected final TypedParamitrisable<S> param;

	public PermissionedParamitriable(final CommandSender sender, final TypedParamitrisable<S> param)
	{
		super(param.getValue());
		this.sender = sender;
		this.param = param;
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		if (!hasAccessPermission(sender, parameter))
			handleNoPermission(parameter);
		this.param.setParameter(parameter);
		setValue(param.getValue());
		if (!hasAccessPermission(sender, value))
			handleNoPermission(parameter);
	}

	@Override
	public List<String> tab(final String parameter)
	{
		final List<String> list = param.tab(parameter);
		final List<String> res = new ArrayList<String>(list.size());
		for (final String string : list)
			if (hasAccessPermission(sender, string))
				res.add(string);
		return res;
	}

	public boolean hasAccessPermission(final CommandSender sender, final String parameter)
	{
		return true;
	}

	public boolean hasAccessPermission(final CommandSender sender, final S value)
	{
		if (value instanceof Named)
			return hasAccessPermission(sender, ((Named) value).getName());
		else if (value instanceof Enum<?>)
			return hasAccessPermission(sender, ((Enum<?>) value).name());
		else
			return hasAccessPermission(sender, value.toString());
	}

	public void handleNoPermission(final String parameter) throws CrazyException
	{
		throw new CrazyCommandPermissionException();
	}
}
