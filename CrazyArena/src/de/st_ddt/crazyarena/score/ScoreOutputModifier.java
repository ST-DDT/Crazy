package de.st_ddt.crazyarena.score;

public interface ScoreOutputModifier
{

	public final static ScoreOutputModifier UNMODIFIEDSCOREOUTPUT = new ScoreOutputModifier()
	{

		@Override
		public String getStringOutput(final String name, final String value)
		{
			return value;
		}

		@Override
		public String getDoubleOutput(final String name, final Double value)
		{
			return value.toString();
		}
	};

	public String getStringOutput(String name, String value);

	public String getDoubleOutput(String name, Double value);
}
