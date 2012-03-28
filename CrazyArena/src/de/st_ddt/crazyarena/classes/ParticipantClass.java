package de.st_ddt.crazyarena.classes;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ParticipantClass
{
	
	protected final String name;
	protected final ArrayList<ItemStack> items;
	protected  ItemStack helmet;
	protected  ItemStack chestplate;
	protected  ItemStack leggins;
	protected  ItemStack boots;
	
	public ParticipantClass(String name)
	{
		super();
		this.name=name;
		items= new  ArrayList<ItemStack>();
		helmet=null;
		chestplate=null;
		leggins=null;
		boots=null;
	}

	public ParticipantClass(String name, ArrayList<ItemStack> items, ItemStack helmet, ItemStack chestplate, ItemStack leggins, ItemStack boots)
	{
		super();
		this.name = name;
		this.items = items;
		this.helmet = helmet.clone();
		this.chestplate = chestplate.clone();
		this.leggins = leggins.clone();
		this.boots = boots.clone();
	}

	public void activate(Player player)
	{
		// TODO Auto-generated method stub
		
	}
	
	
	
	//EDIT
	//public void save();
	
}
