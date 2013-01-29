package de.st_ddt.crazyarena.tasks;

import java.util.Arrays;

import org.bukkit.Bukkit;

import de.st_ddt.crazyarena.CrazyArena;
import de.st_ddt.crazyarena.arenas.Arena;
import de.st_ddt.crazyutil.locales.CrazyLocale;

public class CountDownTask implements Runnable
{

	protected final Arena<?> arena;
	protected final CrazyLocale locale;
	protected final Object[] args;

	public CountDownTask(final Arena<?> arena, final String localePath, final Object[] args)
	{
		super();
		this.arena = arena;
		this.locale = arena.getLocale().getLanguageEntry(localePath);
		this.args = args;
	}

	public CountDownTask(final Arena<?> arena, final CrazyLocale locale, final Object[] args)
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

	public static void startCountDown(final int start, final Arena<?> arena, final String localePath, final Runnable zero)
	{
		startCountDown(start, arena, arena.getLocale().getLanguageEntry(localePath), zero);
	}

	public static void startCountDown(final int start, final Arena<?> arena, final CrazyLocale locale, final Runnable zero)
	{
		startCountDown(start, arena, locale, new Object[1], 0, zero);
	}

	public static void startCountDown(final int start, final Arena<?> arena, final String localePath, final Object[] args, final int timeIndex, final Runnable zero)
	{
		startCountDown(start, arena, arena.getLocale().getLanguageEntry(localePath), args, timeIndex, zero);
	}

	public static void startCountDown(final int start, final Arena<?> arena, final CrazyLocale locale, final Object[] args, final int timeIndex, final Runnable zero)
	{
		for (int i = start; i > 0; i--)
		{
			args[timeIndex] = i;
			Bukkit.getScheduler().scheduleSyncDelayedTask(CrazyArena.getPlugin(), new CountDownTask(arena, locale, Arrays.copyOf(args, args.length)), (start - i) * 20 + 1);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(CrazyArena.getPlugin(), zero, start * 20 + 1);
	}
}
