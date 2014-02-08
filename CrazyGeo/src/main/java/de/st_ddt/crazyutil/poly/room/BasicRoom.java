package de.st_ddt.crazyutil.poly.room;

public abstract class BasicRoom extends PseudoRoom
{

	public abstract void expand(final double x, final double y, final double z);

	public abstract void contract(final double x, final double y, final double z);
}
