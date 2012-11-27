package de.st_ddt.crazysquads.data;

public enum XP_Rules
{
	XP_SHARE(true, false), XP_SHARESILENT(true, true), XP_PRIVATE(false, false), XP_PRIVATESILENT(false, true);

	private final boolean share;
	private final boolean silent;

	private XP_Rules(final boolean share, final boolean silent)
	{
		this.share = share;
		this.silent = silent;
	}

	public boolean isShareEnabled()
	{
		return share;
	}

	public boolean isSilent()
	{
		return silent;
	}
}
