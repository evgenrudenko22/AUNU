package dev.evgenru22.aunu.sabotages;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SabotageElectrical extends Sabotage {
	
	private List<Location> checkers;

	public SabotageElectrical(List<Location> loc, Location locTo, Game game, List<Location> checkers) {
		
		super(loc, locTo);
		
		this.checkers = checkers;
		
		Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(active) {
					
					for(PlayerGame player: game.getPlayers())
						if(player.isLive() && !player.impostor)
							player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999, 0));
					
					for(PlayerGame player: game.getPlayers())
						player.sendTitle("", "§c" + Messages.electricalSabotage);
					
					Cameras camera = game.getMap().getCameras();
					camera.setActive(false);
					
					for(PlayerGame player: camera.getPlayers())
						camera.exit(player);
					
				} else {
					
					for(PlayerGame player: game.getPlayers())
						player.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
					
					Cameras camera = game.getMap().getCameras();
					camera.setActive(true);
					
				}
				
			}
			
		}, 20, 20);
		
		Bukkit.getPluginManager().registerEvents(new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			void playerClick(PlayerInteractEvent e) {
				
				Block block = e.getClickedBlock();
				PlayerGame player = getPlayer(e.getPlayer());
				
				if(player == null || e.getPlayer() != player.getPlayer() || block == null || e.getHand() != EquipmentSlot.HAND || block.getType() != Material.WHITE_WOOL)
					return;
			
				if(block.getType() == Material.WHITE_WOOL)
					block.setType(Material.LIME_WOOL);
				else
					block.setType(Material.WHITE_WOOL);
				
				boolean success = true;
				for(Location loc: checkers)
					if(loc.getBlock().getType() == Material.WHITE_WOOL) {
						
						success = false;
						break;
						
					}
				
				if(success)
					complete();
				
			}
			
		}, Main.plugin);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void startAbsr() {
		
		for(Location block: checkers)
			block.getBlock().setType(Material.WHITE_WOOL);
		
	}

}
