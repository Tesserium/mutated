package com.code2828.mutated;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;

public class M implements ModInitializer {
	public static final SimpleDefaultedRegistry<Mutation> MUTATION = FabricRegistryBuilder
			.createDefaulted(Mutation.class, new Identifier("mutated", "mutation"), new Identifier("mutated", "brisk"))
			.attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final Mutation MUT_BRISK = new Mutation(MutationCategory.GOOD, 0x228A28);
	public static final MutPotionItem POTION_OF_MUTATION = new MutPotionItem(new FabricItemSettings());

	@Override
	public void onInitialize() {
		Registry.register(MUTATION, new Identifier("mutated", "brisk"), MUT_BRISK);
		Registry.register(Registries.ITEM, new Identifier("mutated","potion"), POTION_OF_MUTATION);
	}

}
