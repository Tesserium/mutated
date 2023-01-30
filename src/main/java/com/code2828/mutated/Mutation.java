package com.code2828.mutated;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;

public abstract class Mutation {
	protected final MutationCategory category;
	protected final int color;
	protected final int maxLevel;
	public static final int DEFAULT_MAX_LEVEL = 15;
	private ArrayList<Pair<UUID, Integer>> affectedPlayers = new ArrayList<Pair<UUID, Integer>>();

	public Mutation(MutationCategory category, int color, int maxLevel) {
		this.category = category;
		this.color = color;
		this.maxLevel = maxLevel;
	}

	public Mutation(MutationCategory category, int color) {
		this.category = category;
		this.color = color;
		this.maxLevel = DEFAULT_MAX_LEVEL;
	}

	public MutationCategory getCategory() {
		return category;
	}

	public int getColor() {
		return color;
	}

	public int entity(PlayerEntity playerEntity) {
		for (Pair<UUID, Integer> pair : affectedPlayers) {
			if (playerEntity.getUuid().equals(pair.getLeft()) && pair.getRight() > 0) {
				return pair.getRight();
			}
		}
		return 0;
	}

	/**
	 * @param playerEntity the player
	 * @param level        the level of mutation, 1 by default
	 * @return returns -1 if cannot apply, the level of mutation after applying
	 *         otherwise
	 */
	public int apply(PlayerEntity playerEntity, int level) {
		for (Pair<UUID, Integer> pair : affectedPlayers) {
			if (playerEntity.getUuid().equals(pair.getLeft())) {
				pair.setRight(level);
				return level;
			}
		}
		affectedPlayers.add(new Pair<UUID, Integer>(playerEntity.getUuid(), level));
		return level;
	}

	public int apply(PlayerEntity playerEntity) {
		return apply(playerEntity, 1);
	}

	/**
	 * @param playerEntity the player
	 * @param level
	 * @return -1 if does not exist, 0 if reached max level, the level after
	 *         operation otherwise
	 */
	public int increase(PlayerEntity playerEntity, int level) {
		for (Pair<UUID, Integer> pair : affectedPlayers) {
			if (playerEntity.getUuid().equals(pair.getLeft())) {
				if (pair.getRight() + level >= maxLevel) {
					return 0;
				}
				pair.setRight(pair.getRight() + level);
				return pair.getRight();
			}
		}
		return -1;
	}

	public int increase(PlayerEntity playerEntity) {
		return increase(playerEntity, 1);
	}

	/**
	 * @param playerEntity the player
	 * @param level        levels for removal
	 * @return -1 if the player does not have this mutation, 0 if will be removed, level of mutation after
	 *         removal otherwise
	 */
	public int decrease(PlayerEntity playerEntity, int level) {
		for (Pair<UUID, Integer> pair : affectedPlayers) {
			if (playerEntity.getUuid().equals(pair.getLeft())) {
				if (pair.getRight() <= level) {
					affectedPlayers.remove(pair);
					return 0;
				} else {
					pair.setRight(pair.getRight() - level);
					return pair.getRight();
				}
			}
		}
		return -1;
	}

	public int decrease(PlayerEntity playerEntity) {
		return decrease(playerEntity, 1);
	}

	public int remove(PlayerEntity playerEntity) {
		return decrease(playerEntity, Integer.MAX_VALUE);
	}

}
