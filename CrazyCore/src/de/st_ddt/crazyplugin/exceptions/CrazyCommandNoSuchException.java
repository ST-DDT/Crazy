package de.st_ddt.crazyplugin.exceptions;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyutil.ChatHelper;
import de.st_ddt.crazyutil.locales.Localized;

public class CrazyCommandNoSuchException extends CrazyCommandException
{

	private static final long serialVersionUID = -7687691927850799497L;
	private final String type;
	private final String searched;
	private final Collection<String> alternatives;

	public CrazyCommandNoSuchException(final String type, final String searched)
	{
		super();
		this.type = type;
		this.searched = searched;
		this.alternatives = new ArrayList<String>(0);
	}

	public CrazyCommandNoSuchException(final String type, final String searched, final Collection<String> alternatives)
	{
		super();
		this.type = type;
		this.searched = searched;
		this.alternatives = alternatives;
	}

	public CrazyCommandNoSuchException(final String type, final String searched, final String... alternatives)
	{
		super();
		this.type = type;
		this.searched = searched;
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
	@Localized({ "CRAZYEXCEPTION.COMMAND.NOSUCH $Command$ $Type$ $Searched$", "CRAZYEXCEPTION.COMMAND.NOSUCH.ALTERNATIVES $Alternatives$" })
	public void print(final CommandSender sender, final String header)
	{
		ChatHelper.sendMessage(sender, header, locale, command, type, searched);
		if (alternatives.size() > 0)
			ChatHelper.sendMessage(sender, header, locale.getLanguageEntry("ALTERNATIVES"), ChatHelper.listingString(alternatives));
	}

	public String getType()
	{
		return type;
	}

	public String getSearched()
	{
		return searched;
	}

	public Collection<String> getAlternatives()
	{
		return alternatives;
	}
}
