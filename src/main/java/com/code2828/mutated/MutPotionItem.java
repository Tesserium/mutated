package com.code2828.mutated;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
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
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MutPotionItem extends PotionItem {

	private static final Random RAND = new Random();

	public MutPotionItem() {
		super(new FabricItemSettings().rarity(Rarity.UNCOMMON));
	}

	@Override
	public ItemStack getDefaultStack() {
		return new ItemStack(this);
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
		}

		if (!world.isClient() && playerEntity != null) {
			playerEntity.sendMessage(
					((MutableText) Text.translatable("mutated.msg.urbodychanges")).formatted(Formatting.BLACK));
			// remove some old ones
			int removal = Math.abs(RAND.nextInt()) % 3 + 1;
			int prob1 = Mutations.LIST.size() / removal;
			for (Mutation mut : Mutations.LIST) {
				int l = mut.entity(playerEntity);
				if (l > 0 && RAND.nextInt() % (prob1 <= 0 ? 1 : prob1) == 0) {
					int r = mut.remove(playerEntity);
					if (r >= 0) {
						playerEntity.sendMessage(((MutableText) Text.translatable("mutated.msg.removemut"))
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
						playerEntity.sendMessage(((MutableText) Text.translatable("mutated.msg.applymut"))
								.append(Text.translatable(M.MUTATION.getId(mut).toTranslationKey())));
					} else if (a > 0) {
						playerEntity.sendMessage(((MutableText) Text.translatable("mutated.msg.increasemut"))
								.append(Text.translatable(M.MUTATION.getId(mut).toTranslationKey())
										.append(Text.translatable("mutated.msg.increasemut_to"))
										.append(Text.of(Integer.toString(a)))
										.append(Text.translatable("mutated.msg.increasemut_lev"))));
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
