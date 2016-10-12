package de.st_ddt.crazyannouncer.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import de.st_ddt.crazyannouncer.CrazyAnnouncer;
import de.st_ddt.crazyplugin.commands.CrazyPluginCommandExecutor;
import de.st_ddt.crazyplugin.exceptions.CrazyCommandNoSuchException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.ChatHelperExtended;
import de.st_ddt.crazyutil.NamedRunnable;
import de.st_ddt.crazyutil.modules.permissions.PermissionModule;
import de.st_ddt.crazyutil.paramitrisable.MapParamitrisable;
import de.st_ddt.crazyutil.source.Localized;
import de.st_ddt.crazyutil.source.Permission;

public class CrazyAnnouncerCommandTriggerAction extends CrazyPluginCommandExecutor<CrazyAnnouncer> {

	public CrazyAnnouncerCommandTriggerAction(final CrazyAnnouncer crazyAnnouncer) {
		super(crazyAnnouncer);
	}

	@Override
	@Permission("crazyannouncer.triggeraction")
	@Localized("CRAZYANNOUNCER.COMMAND.TRIGGERACTION {Action}")
	public void command(final CommandSender sender, final String[] args) throws CrazyException {
		final Set<NamedRunnable> actionSet = plugin.getActions();
		final Map<String, NamedRunnable> actions = actionSet.stream().collect(Collectors.toMap(NamedRunnable::getName, Function.identity()));
		final MapParamitrisable<NamedRunnable> actionsMap = new MapParamitrisable<>("Action", actions, null);
		ChatHelperExtended.readParameters(args, new HashMap<>(), actionsMap);
		final NamedRunnable namedRunnable = actionsMap.getValue();
		if (namedRunnable == null) {
			throw new CrazyCommandNoSuchException("Action", "(none)");
		}
		else {
			namedRunnable.run();
			plugin.sendLocaleMessage("COMMAND.TRIGGERACTION", sender, namedRunnable.getName());
		}
	}

	@Override
	public List<String> tab(final CommandSender sender, final String[] args) {
		final Set<NamedRunnable> actionSet = plugin.getActions();
		final Map<String, NamedRunnable> actions = actionSet.stream().collect(Collectors.toMap(NamedRunnable::getName, Function.identity()));
		final MapParamitrisable<NamedRunnable> actionsMap = new MapParamitrisable<>("Action", actions, null);
		return ChatHelperExtended.tabHelp(args, new HashMap<>(), actionsMap);
	}

	@Override
	public boolean hasAccessPermission(final CommandSender sender) {
		return PermissionModule.hasPermission(sender, "crazyannouncer.triggeraction");
	}

}
