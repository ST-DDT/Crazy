package de.st_ddt.crazyarena.tasks;

import org.bukkit.Bukkit;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CountDownTask implements Runnable
{

	protected final Arena<?> arena;
	protected final CrazyLocale locale;
	protected final Object[] args;

	public CountDownTask(Arena<?> arena, String localePath, Object[] args)
	{
		super();
		this.arena = arena;
		this.locale = arena.getLocale().getLanguageEntry(localePath);
		this.args = args;
	}

	public CountDownTask(Arena<?> arena, CrazyLocale locale, Object[] args)
	{
		super();
		this.arena = arena;
		this.locale = locale;
		this.args = args;
	}

	@Override
	public void run()
	{
		arena.broadcastLocaleMessage(false, locale, args);
	}

	public static void startCountDown(int start, Arena<?> arena, String localePath, Runnable zero)
	{
		startCountDown(start, arena, arena.getLocale().getLanguageEntry(localePath), zero);
	}

	public static void startCountDown(int start, Arena<?> arena, CrazyLocale locale, Runnable zero)
	{
		startCountDown(start, arena, locale, new Object[1], 0, zero);
	}

	public static void startCountDown(int start, Arena<?> arena, String localePath, Object[] args, int timeIndex, Runnable zero)
	{
		startCountDown(start, arena, arena.getLocale().getLanguageEntry(localePath), args, timeIndex, zero);
	}

	public static void startCountDown(int start, Arena<?> arena, CrazyLocale locale, Object[] args, int timeIndex, Runnable zero)
	{
		for (int i = start; i > 0; i--)
		{
			args[timeIndex] = i;
			Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyArena.getPlugin(), new CountDownTask(arena, locale, args), (start - i) * 20 + 1);
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(CrazyArena.getPlugin(), zero, start * 20 + 1);
	}
}
