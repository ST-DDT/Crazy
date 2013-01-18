package de.st_ddt.crazyplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import de.st_ddt.crazyplugin.CrazyLightPluginInterface;

public abstract class CrazyEvent<T extends CrazyLightPluginInterface> extends Event
{

	protected final T plugin;

	public CrazyEvent(final T plugin)
	{
		super();
		this.plugin = plugin;
	}

	public T getPlugin()
	{
		return plugin;
	}

	public void callEvent()
	{
		Bukkit.getPluginManager().callEvent(this);
	}

	@Deprecated
	public void callAsyncEvent()
	{
		callEventAsynchronously();
	}

	@SuppressWarnings("deprecation")
	protected void callEventAsynchronously()
	{
		Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, new AsyncEventRunnable(this));
	}

	protected class AsyncEventRunnable implements Runnable
	{

		private final CrazyEvent<T> event;

		public AsyncEventRunnable(final CrazyEvent<T> event)
		{
			super();
			this.event = event;
		}

		@Override
		public void run()
		{
			event.callEvent();
		}
	}
}
