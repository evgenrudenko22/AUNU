package dev.evgenru22.aunu.events;

import dev.evgenru22.aunu.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class GameEndEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		
        return handlers;
        
    }
	

	private Game game;
	private String cause;
    
    public GameEndEvent(Game game, String cause) {
    	
    	this.game = game;
    	this.cause = cause;
    	
    }
    
    public Game getGame() {
    	
    	return game;
    	
    }
    
    public String getCause() {
    	
    	return cause;
    	
    }

    public HandlerList getHandlers() {
    	
        return handlers;
        
    }

}

