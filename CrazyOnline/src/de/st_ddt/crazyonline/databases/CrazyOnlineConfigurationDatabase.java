package de.st_ddt.crazyonline.databases;

import org.bukkit.configuration.ConfigurationSection;

import de.st_ddt.crazyonline.OnlinePlayerData;
import de.st_ddt.crazyutil.databases.ConfigurationDatabase;
import de.st_ddt.crazyutil.databases.ConfigurationDatabaseEntry;

public class CrazyOnlineConfigurationDatabase extends ConfigurationDatabase<OnlinePlayerData, ConfigurationDatabaseEntry<OnlinePlayerData>>
{

	public CrazyOnlineConfigurationDatabase(ConfigurationSection config, String path)
	{
		super(new ConfigurationDatabaseEntry<OnlinePlayerData>(path)
		{

			@Override
			public OnlinePlayerData load(ConfigurationSection rawData)
			{
				return new OnlinePlayerData(rawData);
			}

			@Override
			public void save(OnlinePlayerData data, ConfigurationSection saveTo)
			{
				data.save(saveTo, path);
			}


		}, config, path);
	}
}
