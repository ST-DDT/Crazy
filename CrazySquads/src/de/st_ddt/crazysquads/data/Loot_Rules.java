package de.st_ddt.crazysquads.data;

public enum Loot_Rules
{
	LOOT_SHARE(true, false), LOOT_SHARESILENT(true, true), LOOT_PRIVATE(false, false), LOOT_PRIVATESILENT(false, true);

	private final boolean share;
	private final boolean silent;

	private Loot_Rules(final boolean share, final boolean silent)
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
