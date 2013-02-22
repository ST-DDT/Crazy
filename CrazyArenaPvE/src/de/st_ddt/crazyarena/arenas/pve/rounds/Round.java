package de.st_ddt.crazyarena.arenas.pve.rounds;

import org.bukkit.entity.LivingEntity;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Round extends ConfigurationSaveable
{

	public String getType();

	public boolean isApplyable(int roundNumber);

	public int getPriority();

	public void activate(int roundNumber);

	public void next();

	public void reset();

	public void creatureDeath(LivingEntity entity);
}
