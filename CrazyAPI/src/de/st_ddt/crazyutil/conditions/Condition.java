package de.st_ddt.crazyutil.conditions;

import java.util.Map;
import java.util.TreeMap;

import de.st_ddt.crazyutil.ConfigurationSaveable;
import de.st_ddt.crazyutil.conditions.checker.ConditionChecker;

/**
 * A condition to check certain conditions at runtime.
 */
public interface Condition extends ConfigurationSaveable
{

	public final static Map<String, Class<? extends Condition>> CONDITIONCLASSES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * @return The type of this Condition.<br>
	 *         Required to reloading it after server restart.
	 */
	public String getType();

	/**
	 * This method checks whether this and any possible sub conditions are applicable for the given check.<br>
	 * This method should be executed after loading the entire condition tree.
	 * 
	 * @param clazz
	 *            The class that should be checked, whether it can be used to execute the test.
	 * @return True, if given class can execute the test properly.
	 */
	public boolean isApplicable(Class<? extends ConditionChecker> clazz);

	/**
	 * Checks whether the given property matches this condition.
	 * 
	 * @param checker
	 *            The property that should be checked.
	 * @return True, if the given property matches this condition.
	 */
	public boolean check(ConditionChecker checker);
}
