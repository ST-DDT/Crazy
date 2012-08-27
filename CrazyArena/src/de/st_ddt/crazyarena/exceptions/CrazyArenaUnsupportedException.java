package de.st_ddt.crazyarena.exceptions;

import de.st_ddt.crazyarena.arenas.Arena;

public class CrazyArenaUnsupportedException extends CrazyArenaException
{

	private static final long serialVersionUID = -8372521664628064510L;
	protected final String unsupported;

	public CrazyArenaUnsupportedException(Arena<?> arena, String unsupported)
	{
		super(arena);
		this.unsupported = unsupported;
	}

	@Override
	public String getLangPath()
	{
		return super.getLangPath() + ".UNSUPPORTED";
	}
}
