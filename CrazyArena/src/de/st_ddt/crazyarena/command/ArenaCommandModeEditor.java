package de.st_ddt.crazyarena.command;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyarena.participants.Participant;
import de.st_ddt.crazyplugin.commands.CrazyCommandModeEditor;

public class ArenaCommandModeEditor<S extends Participant<S, ?>> extends CrazyCommandModeEditor<Arena<S>>
{

	public ArenaCommandModeEditor(final Arena<S> arena)
	{
		super(arena);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender)
	{
		return plugin.hasPermission(sender, "mode");
	}
}
