package dev.evgenru22.aunu.tasks;

import java.util.List;

import dev.evgenru22.aunu.amongUs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitTask;


public class Upload_Download extends Longs {
	
	private BukkitTask timerTask;
	private int progress = 0;
	private List<Block> barUpload;
	private List<Block> barDownload;

	public Upload_Download(Location loc, Location locTo, List<Location> nextStepsLocation, List<Location> nextStepsLocTo, Location startLocUpload, Location endLocUpload, Location startLocDownload, Location endLocDownload) {
		
		super(loc, locTo, nextStepsLocation, nextStepsLocTo);
		
		barUpload = blocksFromTwoPoints(startLocUpload, endLocUpload);
		barDownload = blocksFromTwoPoints(startLocDownload, endLocDownload);
		
	}
	
	private void progressTick() {
		
		timerTask = Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				
				if(step == 1) {
					
					if(progress > barUpload.size()-1) {
						
						nextStep();
						return;
						
					}
					
					barUpload.get(barUpload.size()-progress-1).setType(Material.LIME_WOOL);
					
				} else {
					
					if(progress > barDownload.size()-1) {
						
						nextStep();
						return;
						
					}
					
					barDownload.get(barDownload.size()-progress-1).setType(Material.LIME_WOOL);
					
				}
				
				progressTick();
				
				startTimeout();
				
				progress++;
				
			}
			
		}, 30);
		
	}

	@Override
	public void start() {
		
		progress = 0;
		
		progressTick();
		
		if(step == 1)
			for(Block block: barUpload)
				block.setType(Material.WHITE_WOOL);
		else
			for(Block block: barDownload)
				block.setType(Material.WHITE_WOOL);
		
	}

	@Override
	public void stop() {
		
		if(timerTask != null && !timerTask.isCancelled())
			timerTask.cancel();
		
	}

}
