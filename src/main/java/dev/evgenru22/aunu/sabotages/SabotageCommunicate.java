package dev.evgenru22.aunu.sabotages;

import java.util.ArrayList;
import java.util.List;

import dev.evgenru22.aunu.amongUs.Main;
import dev.evgenru22.aunu.amongUs.Messages;
import dev.evgenru22.aunu.game.Cameras;
import dev.evgenru22.aunu.game.Game;
import dev.evgenru22.aunu.game.PlayerGame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import game.PlaySound;
import tasks.Task;

public class SabotageCommunicate extends Sabotage {
	
	private List<Block> bar;
	private List<Block> barTask;
	private int numTask = 0;
	private int currentNum = 0;

	public SabotageCommunicate(List<Location> loc, Location locTo, Game game, Location startBar, Location endBar, Location startBarTask, Location endBarTask) {
		
		super(loc, locTo);
		
		BossBar bossBar = game.getBar();
		
		bar = blocksFromTwoPoints(startBar, endBar);
		barTask = blocksFromTwoPoints(startBarTask, endBarTask);
		
		Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(active) {
					
					bossBar.setVisible(false);
					for(PlayerGame player: game.getPlayers()) {
						
						player.sendTitle("", "§c" + Messages.communicateSabotage);
						PlaySound.SABOTAGE.play(player.getPlayer());
						
					}
					
					Cameras camera = game.getMap().getCameras();
					camera.setActive(false);
					
					for(PlayerGame player: camera.getPlayers())
						camera.exit(player);
					
					for(Task task: game.getTasks())
						task.show = false;
					
				} else {
					
					for(Task task: game.getTasks())
						task.show = true;
					
					bossBar.setVisible(true);
					
					Cameras camera = game.getMap().getCameras();
					camera.setActive(true);
					
				}
				
			}
			
		}, 20, 20);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				PlayerGame player = getPlayer(e.getPlayer());
				
				if(player == null || e.getPlayer() != player.getPlayer() || e.getHand() != EquipmentSlot.HAND || block == null || block.getType() != Material.WHITE_CONCRETE_POWDER)
					return;
				
				if(block.getType() == Material.LIME_CONCRETE_POWDER && currentNum+1 < bar.size())
					currentNum++;
				else if(block.getType() == Material.RED_CONCRETE_POWDER && currentNum > 0)
					currentNum--;
						
				for(Block _block: bar)
					_block.setType(Material.WHITE_CONCRETE_POWDER);
				
				bar.get(currentNum).setType(Material.LIGHT_BLUE_CONCRETE_POWDER);
				
				if(currentNum == numTask)
					complete();
				
			}
			
		}, Main.plugin);
		
	}
	
	private List<Block> blocksFromTwoPoints(Location loc1, Location loc2) {
		
        List<Block> blocks = new ArrayList<Block>();
 
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
 
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
 
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
 
        for(int x = bottomBlockX; x <= topBlockX; x++)
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
                for(int y = bottomBlockY; y <= topBlockY; y++) {
                	
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                   
                    blocks.add(block);
                    
                }

        return blocks;
        
    }

	@SuppressWarnings("deprecation")
	@Override
	public void startAbsr() {
		
		for(Block block: bar)
			block.setType(Material.WHITE_CONCRETE_POWDER);

		for(Block block: barTask)
			block.setType(Material.WHITE_CONCRETE_POWDER);
		
		int random = (int)Math.floor(Math.random() * barTask.size());
		barTask.get(random).setType(Material.LIGHT_BLUE_CONCRETE_POWDER);
		numTask = random;
		
		int random2 = (int)Math.floor(Math.random() * bar.size());
		bar.get(random2).setType(Material.LIGHT_BLUE_CONCRETE_POWDER);
		currentNum = random2;
		
	}

}
