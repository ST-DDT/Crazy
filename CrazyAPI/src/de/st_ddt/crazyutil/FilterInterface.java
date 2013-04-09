package de.st_ddt.crazyutil;

import java.util.Collection;

import de.st_ddt.crazyplugin.exceptions.CrazyException;
import de.st_ddt.crazyutil.paramitrisable.Paramitrisable;

public interface FilterInterface<S> extends Named, Tabbed
{

	@Override
	public String getName();

	public FilterInstanceInterface<S> getInstance();

	public String[] getAliases();

	public interface FilterInstanceInterface<S> extends Named, Paramitrisable
	{

		@Override
		public String getName();

		public String[] getAliases();

		@Override
		public void setParameter(String parameter) throws CrazyException;

		public boolean isActive();

		public void filter(Collection<? extends S> datas);

		public boolean filter(S data);
	}
}
