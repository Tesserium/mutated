package com.code2828.mutated;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;

public abstract class Mutation {
	private final MutationCategory category;
	private final int color;
	private ArrayList<String> affectedPlayers = new ArrayList<String>();
	
	public Mutation(MutationCategory category, int color) {
		this.category = category;
		this.color = color;
	}
	
	public boolean hasEntity(PlayerEntity playerEntity)
	{
		return (affectedPlayers.contains(playerEntity.getUuidAsString()));
	}
	
	public int apply(PlayerEntity playerEntity)
	{
		if(affectedPlayers.contains(playerEntity.toString()))return -1;
		affectedPlayers.add(playerEntity.getUuidAsString());
		return 0;
	}
	
	public int remove(PlayerEntity playerEntity)
	{
		if(!affectedPlayers.contains(playerEntity.toString()))return -1;
		affectedPlayers.remove(playerEntity.getUuidAsString());
		return 0;
	}

}
