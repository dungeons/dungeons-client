package com.kingx.artemis.managers;

import java.util.HashMap;
import java.util.Map;

import com.kingx.artemis.Entity;
import com.kingx.artemis.Manager;
import com.kingx.artemis.utils.Bag;
import com.kingx.artemis.utils.ImmutableBag;


/**
 * You may sometimes want to specify to which player an entity belongs to.
 * 
 * An entity can only belong to a single player at a time.
 * 
 * @author Arni Arent
 *
 */
public class PlayerManager extends Manager {
	private Map<Entity, String> playerByEntity;
	private Map<String, Bag<Entity>> entitiesByPlayer;

	public PlayerManager() {
		playerByEntity = new HashMap<Entity, String>();
		entitiesByPlayer = new HashMap<String, Bag<Entity>>();
	}
	
	public void setPlayer(Entity e, String player) {
		playerByEntity.put(e, player);
		Bag<Entity> entities = entitiesByPlayer.get(player);
		if(entities == null) {
			entities = new Bag<Entity>();
			entitiesByPlayer.put(player, entities);
		}
		entities.add(e);
	}
	
	public ImmutableBag<Entity> getEntitiesOfPlayer(String player) {
		Bag<Entity> entities = entitiesByPlayer.get(player);
		if(entities == null) {
			entities = new Bag<Entity>();
		}
		return entities;
	}
	
	public void removeFromPlayer(Entity e) {
		String player = playerByEntity.get(e);
		if(player != null) {
			Bag<Entity> entities = entitiesByPlayer.get(player);
			if(entities != null) {
				entities.remove(e);
			}
		}
	}
	
	public String getPlayer(Entity e) {
		return playerByEntity.get(e);
	}

	@Override
	protected void initialize() {
	}

	@Override
	public void deleted(Entity e) {
		removeFromPlayer(e);
	}

}
