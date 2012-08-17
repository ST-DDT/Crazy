package de.st_ddt.crazyplugin.exceptions;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;

public class CrazyCommandNoSuchException extends CrazyCommandException
{

	private static final long serialVersionUID = -7687691927850799497L;
	private final String searched;
	private final String type;
	private final Collection<String> alternatives;

	public CrazyCommandNoSuchException(final String type, final String searched)
	{
		super();
		this.searched = searched;
		this.type = type;
		this.alternatives = new ArrayList<String>(0);
	}

	public CrazyCommandNoSuchException(final String type, final String searched, final Collection<String> alternatives)
	{
		super();
		this.searched = searched;
		this.type = type;
		this.alternatives = alternatives;
	}

	public CrazyCommandNoSuchException(final String type, final String searched, final String... alternatives)
	{
		super();
		this.searched = searched;
		this.type = type;
		this.alternatives = new ArrayList<String>(alternatives.length);
		for (final String alternative : alternatives)
			this.alternatives.add(alternative);
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".NOSUCH";
	}

	@Override
	public void print(final CommandSender sender, final String header)
	{
		super.print(sender, header);
		sender.sendMessage(header + locale.getLocaleMessage(sender, "ERROR", type, searched));
		if (alternatives.size() > 0)
		{
			sender.sendMessage(header + locale.getLocaleMessage(sender, "ALTERNATIVESHEAD"));
			sender.sendMessage(header + locale.getLocaleMessage(sender, "ALTERNATIVESLIST", ChatHelper.listingString(alternatives)));
		}
	}

	public Collection<String> getAlternatives()
	{
		return alternatives;
	}
}
