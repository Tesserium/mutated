package com.code2828.mutated;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MutPotionItem extends PotionItem {

	private static final Random RAND = new Random();

	public MutPotionItem(Settings settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
		}

		if (!world.isClient()) {
			// remove some old ones
			int removal = Math.abs(RAND.nextInt()) % 3 + 1;
			int prob1 = Mutations.LIST.size() / removal;
			for (Mutation mut : Mutations.LIST) {
				int l = mut.entity(playerEntity);
				if (l > 0 && RAND.nextInt() % (prob1 <= 0 ? 1 : prob1) == 0) {
					int r = mut.remove(playerEntity);
					if (r >= 0) {
						playerEntity.sendMessage(((MutableText) Text.of("Removed mutation "))
								.append(Text.translatable(M.MUTATION.getId(mut).toTranslationKey())));
					}
				}
			}
			// apply some new ones
			int addition = Math.abs(RAND.nextInt()) % 3 + 1;
			int prob2 = Mutations.LIST.size() / addition;
			for (Mutation mut : Mutations.LIST) {
				if (RAND.nextInt() % (prob2 <= 0 ? 1 : prob2) == 0) {
					int a = mut.increase(playerEntity);
					if (a < 0) {
						mut.apply(playerEntity);
						playerEntity.sendMessage(((MutableText) Text.of("Applied mutation "))
								.append(Text.translatable(M.MUTATION.getId(mut).toTranslationKey())));
					} else if (a > 0) {
						playerEntity.sendMessage(((MutableText) Text.of("Increased level of mutation "))
								.append(Text.translatable(M.MUTATION.getId(mut).toTranslationKey())
										.append(Text.of(" to " + Integer.toString(a)))));
					}
				}
			}
		}

		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}

		// leaves an empty glass bottle
		if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		user.emitGameEvent(GameEvent.DRINK);
		return stack;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		tooltip.add(Text.translatable("item.mutated.potion.tooltip").formatted(Formatting.BOLD));
	}

}
