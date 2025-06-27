package dev.evgenru22.aunu.tasks;

import java.util.List;

import dev.evgenru22.aunu.amongUs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class ClearOxygen extends Task {
	
	private List<Block> pole;
	private Block currentBlock;

	public ClearOxygen(Location loc, Location locTo, Location start, Location end) {
		
		super(loc, locTo);
		
		pole = blocksFromTwoPoints(start, end);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_WOOL)
					return;
				
				if(block.getType() == Material.YELLOW_WOOL)
					currentBlock = block;
				
				if(block.getType() == Material.WHITE_WOOL && currentBlock != null) {
					
					currentBlock.setType(Material.GRAY_WOOL);
					currentBlock = null;
					startTimeout();
					
				}
				
				for(Block _block: pole)
					if(Tag.WOOL.isTagged(_block.getType()))
						return;
				
				complete(true);
				
			}
			
		}, Main.plugin);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void start() {
		
		for(Block block: pole)
			block.setType(Material.WHITE_WOOL);
		
		for(int i = 0; i < 7; i++) {
			
			int random = (int)Math.floor(Math.random() * pole.size());
			
			pole.get(random).setType(Material.YELLOW_WOOL);
			
		}
		
	}

	@Override
	public void stop() {}

}
