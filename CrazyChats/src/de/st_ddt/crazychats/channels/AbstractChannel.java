package de.st_ddt.crazychats.channels;

import java.util.ArrayList;
import java.util.List;

import de.st_ddt.crazychats.CrazyChats;

public abstract class AbstractChannel implements ChannelInterface
{

	protected static CrazyChats plugin;
	protected final String name;
	protected final List<String> aliases = new ArrayList<String>();

	public static void setPlugin(final CrazyChats plugin)
	{
		AbstractChannel.plugin = plugin;
	}

	public AbstractChannel(final String name)
	{
		super();
		this.name = name;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public final List<String> getAliases()
	{
		return aliases;
	}
}
