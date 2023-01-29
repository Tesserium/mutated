package com.code2828.mutated.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.code2828.mutated.M;
import com.code2828.mutated.Mutations;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		// TODO Auto-generated constructor stub
	}

	@ModifyVariable(at = @At("HEAD"), method = "addExperience(Lint;)V")
	private int alterExperience(int experience) {
		PlayerEntity thisEntity = (PlayerEntity) (Object) this;
		double e = experience;
		int j = Mutations.EXPERIENCE_BOOST.entity(thisEntity);
		int k = Mutations.EXPERIENCE_DEPLETION.entity(thisEntity);
		if (j > 0) {
			e *= Math.pow(2, Math.log(j + 1));
		}
		if (k > 0) {
			e /= Math.pow(2, Math.log(k + 1));
		}
		return (int) e;
	}
}
