package com.code2828.mutated;

import java.util.List;

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

public class MutPotionItem extends PotionItem{

	public MutPotionItem(Settings settings) {
		super(settings);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
		}
		
		if(!world.isClient())
		{
			Mutations.EXPERIENCE_BOOST.apply(playerEntity);
			//Mutations.FALL_VULNERABILITY.apply(playerEntity);
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
