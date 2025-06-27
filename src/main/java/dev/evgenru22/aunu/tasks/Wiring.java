package dev.evgenru22.aunu.tasks;

import java.util.List;

import dev.evgenru22.aunu.amongUs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class Wiring extends Longs {
	
	private List<Block> blue;
	private List<Block> red;
	private List<Block> yellow;
	private List<Block> green;

	public Wiring(Location loc, Location locTo, List<Location> nextStepsLocation, List<Location> nextStepsLocTo, List<Location> wiresStart, List<Location> wiresEnd) {
		
		super(loc, locTo, nextStepsLocation, nextStepsLocTo);
		
		blue = blocksFromTwoPoints(wiresStart.get(0), wiresEnd.get(0));
		red = blocksFromTwoPoints(wiresStart.get(1), wiresEnd.get(1));
		yellow = blocksFromTwoPoints(wiresStart.get(2), wiresEnd.get(2));
		green = blocksFromTwoPoints(wiresStart.get(3), wiresEnd.get(3));
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_WOOL)
					return;
				
				Material type = Material.WHITE_WOOL;
				
				for(Block _block: blue)
					if(_block.getLocation().distance(block.getLocation()) < 1)
						type = Material.BLUE_WOOL;
				for(Block _block: red)
					if(_block.getLocation().distance(block.getLocation()) < 1)
						type = Material.RED_WOOL;
				for(Block _block: yellow)
					if(_block.getLocation().distance(block.getLocation()) < 1)
						type = Material.YELLOW_WOOL;
				for(Block _block: green)
					if(_block.getLocation().distance(block.getLocation()) < 1)
						type = Material.LIME_WOOL;
				
				if(type != Material.WHITE_WOOL) {
					
					block.setType(type);
					startTimeout();
					
				}
				
				for(Block _block: blue)
					if(_block.getType() == Material.WHITE_WOOL)
						return;
				for(Block _block: red)
					if(_block.getType() == Material.WHITE_WOOL)
						return;
				for(Block _block: yellow)
					if(_block.getType() == Material.WHITE_WOOL)
						return;
				for(Block _block: green)
					if(_block.getType() == Material.WHITE_WOOL)
						return;
				
				nextStep();
				
			}
			
		}, Main.plugin);
		
	}

	@Override
	public void start() {
		
		for(Block block: blue)
			block.setType(Material.WHITE_WOOL);
		for(Block block: red)
			block.setType(Material.WHITE_WOOL);
		for(Block block: yellow)
			block.setType(Material.WHITE_WOOL);
		for(Block block: green)
			block.setType(Material.WHITE_WOOL);
		
	}

	@Override
	public void stop() {}
	
}
