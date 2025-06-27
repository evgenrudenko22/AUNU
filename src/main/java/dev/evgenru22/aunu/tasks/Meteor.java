package dev.evgenru22.aunu.tasks;

import java.util.List;

import dev.evgenru22.aunu.amongUs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class Meteor extends Task {
	
	private List<Block> blocks;
	private int progress = 0;

	public Meteor(Location loc, Location locTo, Location startPole, Location endPole, boolean visual) {
		
		super(loc, locTo);
		
		blocks = blocksFromTwoPoints(startPole, endPole);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || block.getType() != Material.STONE)
					return;
			
				progress++;
				startTimeout();
				
				if(visual)
					lastLocPlayer.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, loc, 1);
				
				if(progress < 20)
					newMeteor();
				else
					complete(true);
				
			}
			
		}, Main.plugin);
		
	}
	
	@SuppressWarnings("deprecation")
	private void newMeteor() {
		
		for(Block block: blocks)
			block.setType(Material.BLACK_WOOL);
		
		Block block = blocks.get((int)Math.floor(Math.random() * blocks.size()));

		block.setType(Material.STONE);
		
	}

	@Override
	public void start() {
		
		newMeteor();
		progress = 0;
		
	}

	@Override
	public void stop() {}
	
}
