package de.st_ddt.crazylogin.events;

public enum LoginFailReason
{
	/**
	 * The user has entered a wrong password.
	 */
	WRONG_PASSWORD,
	/**
	 * The user does not have an account yet.
	 */
	NO_ACCOUNT,
	/**
	 * The {@link CrazyLoginPreLoginEvent} has been cancelled.
	 */
	CANCELLED;
}
