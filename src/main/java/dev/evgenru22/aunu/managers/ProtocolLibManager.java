package dev.evgenru22.aunu.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.comphenix.protocol.events.PacketContainer;
import dev.evgenru22.aunu.amongUs.Main;
import dev.evgenru22.aunu.game.Lobby;
import dev.evgenru22.aunu.game.PlayerGame;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

public class ProtocolLibManager {
	
	public static ProtocolManager protocollib;
	
	public static void init() {
		
		ProtocolLibManager.protocollib = ProtocolLibrary.getProtocolManager();
		
		cancelThenPlayerDoingAction();
		hiddenItemInHand();
		
		Main.plugin.getLogger().info("ProtocolLib successfully connected!");
		
	}

	private static void cancelThenPlayerDoingAction() {
		
		protocollib.addPacketListener(new PacketAdapter(Main.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_HEAD_ROTATION, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_TELEPORT, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_DESTROY, PacketType.Play.Server.ANIMATION, PacketType.Play.Server.ENTITY_STATUS) {
			
			@Override
		    public void onPacketSending(PacketEvent event) {
				
				List<Integer> entityId = new ArrayList<Integer>();
				Lobby lobby = Lobby.getLobby(event.getPlayer());
				if(lobby == null || lobby.getGame() == null)
					return;
				for(PlayerGame player: lobby.getGame().getPlayers())
					if(player.getAction() != null)
						entityId.add(player.getPlayer().getEntityId());
				
				if(PacketType.Play.Server.ENTITY_DESTROY == event.getPacketType()) {
					
					int[] lastIds = (int[])event.getPacket().getIntegerArrays().read(0);
					
					int[] _ids = new int[((int[])event.getPacket().getIntegerArrays().read(0)).length];
					
					for(int i = 0; i < lastIds.length; i++)
						if(!entityId.contains(lastIds[i]))
							_ids[i] = lastIds[i];
					
					event.getPacket().getIntegerArrays().write(0, _ids);
					
					return;
					
				}
				
				if(entityId.contains(event.getPacket().getIntegers().read(0)))
					event.setCancelled(true);
				
		    }
			
		});
		
	}
	
	private static void hiddenItemInHand() {
		
		protocollib.addPacketListener(new PacketAdapter(Main.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EQUIPMENT) {
			
			@Override
		    public void onPacketSending(PacketEvent event) {
				
				Lobby lobby = Lobby.getLobby(event.getPlayer());
				if(lobby == null || lobby.getGame() == null || !lobby.getGame().isStart() || event.getPacket().getItemSlots().read(0) != ItemSlot.MAINHAND)
					return;
				
				event.setCancelled(true);
				
		    }
			
		});
		
	}
	
	public static void setValue(Object packet, String name, Object value) {
		
		try {
			
			Field field = packet.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(packet, value);
			
		} catch (Exception e) {}
		
	}
	
	public static PacketContainer packetNamedSpawnEntitySpawn(Player p, int id) {

		PacketContainer packet = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
		
		Location loc = p.getPlayer().getLocation();
		packet.getIntegers().write(0, id);
		packet.getDoubles().write(1, loc.getY()-1.43);
		byte yawByte = (byte) (0 / 360);
		byte pitchByte = (byte) ((-90 % 360) * 256 / 360);
		packet.getBytes().write(0, yawByte);
		packet.getBytes().write(1, pitchByte);
		
		return packet;
		
	}
	
	public static PacketContainer packetEntityDestroy(int id) {
		PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
		packetContainer.getIntegerArrays().write(0, new int[]{id});

		return packetContainer;
		
	}
	
}
