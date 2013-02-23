package de.st_ddt.crazyarena.exceptions;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyutil.source.Localized;

public class CrazyArenaUnsupportedException extends CrazyArenaException
{

	private static final long serialVersionUID = -8372521664628064510L;
	protected final String unsupported;

	public CrazyArenaUnsupportedException(final Arena<?> arena, final String unsupported)
	{
		super(arena);
		this.unsupported = unsupported;
	}

	@Override
	@Localized("CRAZYARENA.ARENA_DEFAULT.EXCEPTION.UNSUPPORTED $Name$ $Type$ $Status$")
	public String getLangPath()
	{
		return super.getLangPath() + ".UNSUPPORTED";
	}
}
