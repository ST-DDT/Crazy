package de.st_ddt.crazylogin.crypt;

import de.st_ddt.crazyutil.ConfigurationSaveable;

public interface Encryptor extends ConfigurationSaveable
{

	/**
	 * Used when loading a Encryptor from Config
	 * 
	 * @param plugin
	 *            This is CrazyLogin
	 * @param config
	 *            The config section this encryptor is loaded from. (May be null)
	 */
	// public Encryptor( LoginPlugin<? extends LoginData> plugin, ConfigurationSection config)
	// {
	// }
	/**
	 * Used when creating an Encryptor with Commands
	 * 
	 * @param plugin
	 *            This is CrazyLogin
	 * @param args
	 *            The parameters to create this encryptor.
	 */
	// public Encryptor( LoginPlugin<? extends LoginData> plugin, String[] args) throws CrazyException
	// {
	// }
	/**
	 * Encrypts the password.
	 * 
	 * @param name
	 *            The name this password belongs too.
	 * @param salt
	 *            The salt used to increase security. (Default length=10)
	 * @param password
	 *            The password that should be encrypted.
	 * @return The encrypted password
	 */
	public String encrypt(String name, String salt, String password);

	/**
	 * Check whether the password is correct.
	 * 
	 * @param name
	 *            The name the password belongs too.
	 * @param password
	 *            The password that should be checked.
	 * @param encrypted
	 *            The encrypted password to be compared to.
	 * @return True, if the password is correct otherwise false.
	 */
	public boolean match(String name, String password, String encrypted);

	/**
	 * @return The unique name of this algorithm.
	 */
	public String getAlgorithm();

	/**
	 * @param encryptor
	 *            The encryptor that should be compared.
	 * @return True, if encryptor creates the same hashes as this encryptor, otherwise false.
	 */
	public boolean equals(Encryptor encryptor);
}
