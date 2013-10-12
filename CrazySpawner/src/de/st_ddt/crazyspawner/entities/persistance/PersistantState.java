package de.st_ddt.crazyspawner.entities.persistance;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;

public interface PersistantState extends ConfigurationSerializable
{

	public void attachTo(Entity entity, PersistanceManager manager);
}
