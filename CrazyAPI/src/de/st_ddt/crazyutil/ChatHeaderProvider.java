package de.st_ddt.crazyutil;

/**
 * This object provides a chatheader.<br>
 * These can be used to add a prefix to messages that this object sends to chat.
 */
public interface ChatHeaderProvider
{

	/**
	 * @return The chatHeader used to send a message to chat.
	 */
	public String getChatHeader();
}
