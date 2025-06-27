package dev.evgenru22.aunu.tasks;

import java.util.ArrayList;
import java.util.List;

import dev.evgenru22.aunu.amongUs.Main;
import dev.evgenru22.aunu.amongUs.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class CalibrateDistr extends Task {
	
	private List<Block> pole = new ArrayList<Block>();
	private List<Block> blocks;
	private List<Block> success;

	public CalibrateDistr(Location loc, Location locTo, Location poleStart, Location poleEnd) {
		
		super(loc, locTo);
		
		for(Block block: blocksFromTwoPoints(poleStart, poleEnd))
			if(block.getType() == Material.WHITE_WOOL)
				pole.add(block);
		
		Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {@Override public void run() {tick();}}, 20, 20);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_CONCRETE_POWDER)
					return;
			
				boolean yes = false;
				for(Block _block: pole)
					if(!success.contains(_block) && _block.getType() == Material.BLACK_CONCRETE_POWDER && _block.getLocation().distance(block.getLocation()) < 1.1) {
						
						success.add(_block);
						blocks.remove(_block);
						
						yes = true;
						break;
						
					}
				
				startTimeout();
				
				if(!yes) {
					
					player.sendMessage("§b§o" + Messages.calibrateDistrTask);
					complete(false);
					
				}
				
				if(blocks.size() < 1)
					complete(true);
				
			}
			
		}, Main.plugin);
		
	}
	
	@SuppressWarnings("deprecation")
	private void tick() {
		
		if(!inProgress)
			return;
		
		List<Block> list = new ArrayList<Block>();
		list.addAll(blocks);
		
		for(Block block: list) {
			
			int num = pole.indexOf(block)+1;
			
			block.setType(Material.WHITE_WOOL);
			
			if(num+1 > pole.size())
				num = 0;
			
			Block _block = pole.get(num);
			
			blocks.set(blocks.indexOf(block), pole.get(num));
			
			_block.setType(Material.BLACK_WOOL);
			
		}
		
		for(Block block: success)
			block.setType(Material.LIME_WOOL);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void start() {
		
		for(Block block: pole)
			block.setType(Material.WHITE_WOOL);
		
		blocks = new ArrayList<Block>();
		success = new ArrayList<Block>();
		
		for(int i = 0; i < 4; i++)
			blocks.add(pole.get(i));
		
	}

	@Override
	public void stop() {}

}
