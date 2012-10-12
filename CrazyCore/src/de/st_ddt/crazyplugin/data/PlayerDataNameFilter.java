package de.st_ddt.crazyplugin.data;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.st_ddt.crazyplugin.exceptions.CrazyCommandErrorException;
import de.st_ddt.crazyplugin.exceptions.CrazyException;

public class PlayerDataNameFilter<S extends PlayerDataInterface> extends PlayerDataFilter<S>
{

	public PlayerDataNameFilter()
	{
		super("name", new String[] { "name" });
	}

	@Override
	public FilterInstance getInstance()
	{
		return new FilterInstance()
		{

			private Pattern pattern = Pattern.compile(".*");

			@Override
			public void setParameter(final String parameter) throws CrazyException
			{
				try
				{
					pattern = Pattern.compile(parameter.replaceAll("\\*", ".\\*"), Pattern.CASE_INSENSITIVE);
				}
				catch (final PatternSyntaxException e)
				{
					throw new CrazyCommandErrorException(e);
				}
			}

			@Override
			public boolean filter(final S data)
			{
				return pattern.matcher(data.getName()).matches();
			}
		};
	}
}
