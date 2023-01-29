package com.code2828.mutated;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
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
			for (Mutation mut : Mutations.LIST) {
				int l = mut.entity(playerEntity);
				if (l > 0 && RAND.nextBoolean()) {
					mut.remove(playerEntity);
				}
			}
			// apply some new ones
			if (RAND.nextBoolean()) {
				int a = Mutations.EXPERIENCE_BOOST.increase(playerEntity);
				if (a < 0)
					Mutations.EXPERIENCE_BOOST.apply(playerEntity);
			} else {
				int b = Mutations.EXPERIENCE_DEPLETION.increase(playerEntity);
				if (b < 0)
					Mutations.EXPERIENCE_DEPLETION.apply(playerEntity);
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
