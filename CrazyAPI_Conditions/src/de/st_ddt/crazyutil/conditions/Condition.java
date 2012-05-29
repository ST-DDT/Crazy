package de.st_ddt.crazyutil.conditions;

import java.util.Collection;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Condition<T> extends ConfigurationSaveable
{

	// public ConditionBase(ConfigurationSection config)
	// public ConditionBase()
	/**
	 * Save this Condition to config
	 * 
	 * @param config
	 *            The Config to save to
	 * @param path
	 *            The path in the config to save the data (path should end with ".")
	 */
	public abstract void save(ConfigurationSection config, String path);

	/**
	 * A name used when saving (extended with index)
	 * 
	 * @return The name used for saving.
	 */
	public abstract String getTypeIdentifier();

	/**
	 * Check whether tester matches this conditions.
	 * 
	 * @param tester
	 *            The testers to check this condition.
	 * @return whether the tester matches this condition
	 */
	public abstract boolean match(T tester);

	/**
	 * Check whether tester matches this conditions.
	 * 
	 * @param testers
	 *            The tester to check this condition.
	 * @return whether all of the testers matches this condition
	 */
	public abstract boolean match(T[] testers);

	/**
	 * Check whether tester matches this conditions.
	 * 
	 * @param testers
	 *            The testers to check this condition.
	 * @return whether all of the testers matches this condition
	 */
	public abstract boolean match(List<? extends T> testers);

	/**
	 * Returns a Collection containing all testers matching the condition.
	 * 
	 * @param testers
	 *            Testers to check the conditions.
	 * @return A Collection containing all testers matching the condition.
	 */
	public abstract Collection<T> getMatching(T[] testers);

	/**
	 * Returns a Collection containing all testers matching the condition.
	 * 
	 * @param testers
	 *            Testers to check the conditions.
	 * @return A Collection containing all testers matching the condition.
	 */
	public abstract Collection<T> getMatching(Collection<? extends T> testers);
}
