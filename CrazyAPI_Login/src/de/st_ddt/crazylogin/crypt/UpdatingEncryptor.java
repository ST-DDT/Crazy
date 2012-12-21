package de.st_ddt.crazylogin.crypt;

public interface UpdatingEncryptor extends Encryptor
{

	/**
	 * Encrypts the password. (Also used when using a new encryption algorithm)
	 * 
	 * @param name
	 *            The name this password belongs too.
	 * @param salt
	 *            The salt used to increase security. (Default length=10)
	 * @param password
	 *            The password that should be encrypted.
	 * @return The encrypted password
	 */
	@Override
	public String encrypt(String name, String salt, String password);
}
