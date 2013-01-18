package de.st_ddt.crazyutil;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyplugin.data.ParameterData;
import de.st_ddt.crazyutil.locales.CrazyLocale;
import de.st_ddt.crazyutil.locales.Localized;

public interface ListFormat
{

	// @ // @Localized("PATH $CurrentPage$ $MaxPage$ $ChatHeader$ $DateTime$ $CustomArgs...$")
	/**
	 * Used in {@link CrazyPages#show(CommandSender) CrazyPages} as message head. </br> $0$ = current Page </br> $1$ = max Page </br> $2$ = chatHeader </br> $3$ = current date
	 * 
	 * @param target
	 *            The target of the list message.
	 * @return Returns the list header.
	 */
	public String headFormat(CommandSender target);

	// @ // @Localized("PATH $Index$ $Entry$ $ChatHeader$")
	/**
	 * Used to show and seperate entries. </br> $0$ = index </br> $1$ = entry </br> $2$ = chatHeader
	 * 
	 * @param target
	 *            The target of the list message.
	 * @return Returns the list format.
	 */
	public String listFormat(CommandSender target);

	// @ // Localized("PATH $Name$ ...")
	/**
	 * Used to format entries (Works only with {@link ParameterData}. Used in {@link #putArgsPara(String, ParameterData) putArgsPara}. Should not be null when using ParameterData.
	 * 
	 * @param target
	 *            The target of the list message.
	 * @return Returns the entry format.
	 */
	public String entryFormat(CommandSender target);

	public static final ListFormat DEFAULTFORMAT = new ListFormat()
	{

		@Override
		@Localized("CRAZYPLUGIN.LIST.HEADER")
		public String headFormat(final CommandSender sender)
		{
			return CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.HEADER").getLanguageText(sender);
		}

		@Override
		@Localized("CRAZYPLUGIN.LIST.LISTFORMAT")
		public String listFormat(final CommandSender sender)
		{
			return CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.LISTFORMAT").getLanguageText(sender);
		}

		@Override
		@Localized("CRAZYPLUGIN.LIST.ENTRYFORMAT")
		public String entryFormat(final CommandSender sender)
		{
			return CrazyLocale.getLocaleHead().getLanguageEntry("CRAZYPLUGIN.LIST.ENTRYFORMAT").getLanguageText(sender);
		}
	};
}
