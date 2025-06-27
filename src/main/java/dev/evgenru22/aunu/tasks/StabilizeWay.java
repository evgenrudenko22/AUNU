package dev.evgenru22.aunu.tasks;

import dev.evgenru22.aunu.amongUs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class StabilizeWay extends Task {
	
	private boolean active = false;

	public StabilizeWay(Location loc, Location locTo) {
		
		super(loc, locTo);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_WOOL)
					return;
				
				if(block.getType() == Material.BLUE_WOOL)
					active = true;
				
				if(block.getType() == Material.WHITE_WOOL && active)
					complete(true);
				
			}
			
		}, Main.plugin);
		
	}

	@Override
	public void start() {
		
		active = false;
		
	}

	@Override
	public void stop() {}
	
}
