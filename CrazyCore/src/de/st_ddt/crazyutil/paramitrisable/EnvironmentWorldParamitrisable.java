package de.st_ddt.crazyutil.paramitrisable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class EnvironmentWorldParamitrisable extends WorldParamitrisable
{

	private final Set<Environment> environments = new HashSet<Environment>();

	public EnvironmentWorldParamitrisable(final CommandSender sender, final Collection<Environment> environments)
	{
		super(sender);
		this.environments.addAll(environments);
	}

	public EnvironmentWorldParamitrisable(final CommandSender sender, final Environment... environments)
	{
		super(sender);
		for (final Environment environment : environments)
			this.environments.add(environment);
	}

	public EnvironmentWorldParamitrisable(final Player player, final Collection<Environment> environments)
	{
		super(player);
		this.environments.addAll(environments);
	}

	public EnvironmentWorldParamitrisable(final Player player, final Environment... environments)
	{
		super(player);
		for (final Environment environment : environments)
			this.environments.add(environment);
	}

	public EnvironmentWorldParamitrisable(final World defaultValue, final Collection<Environment> environments)
	{
		super(defaultValue);
		this.environments.addAll(environments);
	}

	public EnvironmentWorldParamitrisable(final World defaultValue, final Environment... environments)
	{
		super(defaultValue);
		for (final Environment environment : environments)
			this.environments.add(environment);
	}

	@Override
	public void setParameter(final String parameter) throws CrazyException
	{
		super.setParameter(parameter);
		if (!environments.contains(value.getEnvironment()))
			throw new CrazyCommandNoSuchException("World", parameter, tab(parameter));
	}

	@Override
	public List<String> tab(final String parameter)
	{
		return getWorldNames(getWorldsByEnvironment(environments));
	}
}
