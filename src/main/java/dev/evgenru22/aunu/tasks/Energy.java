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


public class Energy extends Longs {
	
	private List<Block> slider;

	public Energy(Location loc, Location locTo, List<Location> nextStepsLocation, List<Location> nextStepsLocTo, Location startSlider, Location endSlider, Location button) {
		
		super(loc, locTo, nextStepsLocation, nextStepsLocTo);
		
		slider = blocksFromTwoPoints(startSlider, endSlider);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_WOOL)
					return;
				
				if(step == 1) {
					
					if(slider.contains(block) && block.getType() == Material.WHITE_WOOL)
						block.setType(Material.YELLOW_WOOL);
					
					for(Block _block: slider)
						if(_block.getType() == Material.WHITE_WOOL)
							return;
					
					nextStep();
					
					return;
					
				}
				
				if(button.distance(block.getLocation()) < 1)
					nextStep();
				
			}
			
		}, Main.plugin);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void start() {
		
		for(Block block: slider)
			block.setType(Material.WHITE_WOOL);
		
	}

	@Override
	public void stop() {}
	
}
