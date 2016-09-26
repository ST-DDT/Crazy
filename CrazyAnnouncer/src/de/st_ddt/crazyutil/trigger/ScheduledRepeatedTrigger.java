package de.st_ddt.crazyutil.trigger;

import java.util.Date;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import de.st_ddt.crazyutil.NamedRunnable;

public class ScheduledRepeatedTrigger extends ScheduledTrigger {

	protected long interval;
	protected int repeat;

	public ScheduledRepeatedTrigger(final ConfigurationSection config, final Set<NamedRunnable> actionlist, final JavaPlugin plugin) {
		super(config, actionlist, plugin);
		this.interval = config.getInt("interval", 1000);
		this.repeat = config.getInt("repeat", 0);
		final long now = System.currentTimeMillis();
		if (repeat > 0) {
			this.repeat = (int) Math.max((repeat - date.getTime() - now) / interval, 0);
		}
		if (repeat != 0) {
			final long executionOffset = date.getTime() % interval;
			final long nowOffset = now % interval;
			if (nowOffset < executionOffset) {
				date.setTime(now - nowOffset + executionOffset);
			}
			else {
				date.setTime(now - nowOffset + executionOffset + interval);
			}
		}
	}

	public ScheduledRepeatedTrigger(final String name, final Set<NamedRunnable> actionlist, final JavaPlugin plugin, final Date date, final long interval, final int repeat) {
		super(name, actionlist, plugin, date);
		this.interval = interval;
		this.repeat = repeat;
		final long now = System.currentTimeMillis();
		if (repeat > 0) {
			this.repeat = (int) Math.max((repeat - date.getTime() - now) / interval, 0);
		}
		if (repeat != 0) {
			final long executionOffset = date.getTime() % interval;
			final long nowOffset = now % interval;
			if (nowOffset < executionOffset) {
				date.setTime(now - nowOffset + executionOffset);
			}
			else {
				date.setTime(now - nowOffset + executionOffset + interval);
			}
		}
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (enabled != this.enabled) {
			if (enabled) {
				super.setEnabled(enabled);
			}
		}
	}

	@Override
	public void run() {
		super.run();
		if (repeat == 0) {
			return;
		}
		date = new Date(date.getTime() + interval);
		if (repeat != -1) {
			repeat--;
		}
		register();
	}

	@Override
	public void save(final ConfigurationSection config, final String path) {
		super.save(config, path);
		config.set(path + "repeat", repeat);
		config.set(path + "interval", interval);
	}
}
