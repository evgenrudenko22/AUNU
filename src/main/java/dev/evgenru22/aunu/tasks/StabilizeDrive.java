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


public class StabilizeDrive extends Longs {
	
	private List<Block> blocks;
	private int num = 0;

	public StabilizeDrive(Location loc, Location locTo, List<Location> nextStepsLocation, List<Location> nextStepsLocTo, Location start, Location end) {
		
		super(loc, locTo, nextStepsLocation, nextStepsLocTo);
		
		this.blocks = blocksFromTwoPoints(start, end);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_CONCRETE_POWDER)
					return;
			
				blocks.get(blocks.size()-num-1).setType(Material.WHITE_CONCRETE_POWDER);
				
				num++;
				
				blocks.get(blocks.size()-num-1).setType(Material.LIME_CONCRETE_POWDER);
				
				if(num+2 > blocks.size())
					nextStep();
				
			}
			
		}, Main.plugin);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void start() {
		
		num = 0;
		
		for(Block block: blocks)
			block.setType(Material.WHITE_CONCRETE_POWDER);
		
		blocks.get(blocks.size()-1).setType(Material.LIME_CONCRETE_POWDER);
		
	}

	@Override
	public void stop() {}
	
}
