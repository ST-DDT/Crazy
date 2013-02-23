package de.st_ddt.crazysquads.data;

public enum ShareRules
{
	SHARE(true, false),
	SHARESILENT(true, true),
	PRIVATE(false, false),
	PRIVATESILENT(false, true);

	private final boolean share;
	private final boolean silent;

	private ShareRules(final boolean share, final boolean silent)
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
