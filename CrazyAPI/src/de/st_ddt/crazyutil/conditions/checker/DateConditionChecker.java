package de.st_ddt.crazyutil.conditions.checker;

import java.util.Date;

public interface DateConditionChecker extends ConditionChecker
{

	public Date getDate();

	public class SimpleDateConditionChecker implements DateConditionChecker
	{

		private final Date date;

		public SimpleDateConditionChecker(final Date date)
		{
			super();
			this.date = date;
		}

		@Override
		public Date getDate()
		{
			return date;
		}
	}
}
