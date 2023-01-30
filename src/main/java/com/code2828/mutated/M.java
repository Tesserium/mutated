package com.code2828.mutated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;

public class M implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("mutated");
	protected static final SimpleDefaultedRegistry<Mutation> MUTATION = FabricRegistryBuilder
			.createDefaulted(Mutation.class, new Identifier("mutated", "mutation"),
					new Identifier("mutated", "experience_boost"))
			.attribute(RegistryAttribute.SYNCED).buildAndRegister();
	protected static final MutPotionItem POTION_OF_MUTATION = new MutPotionItem(new FabricItemSettings());

	@Override
	public void onInitialize() {
		Registry.register(MUTATION, new Identifier("mutated", "experience_boost"), Mutations.EXPERIENCE_BOOST);
		Registry.register(MUTATION, new Identifier("mutated", "experience_depletion"), Mutations.EXPERIENCE_DEPLETION);
		Registry.register(MUTATION, new Identifier("mutated", "fall_vulnerability"), Mutations.FALL_VULNERABILITY);
		Registry.register(Registries.ITEM, new Identifier("mutated", "potion"), POTION_OF_MUTATION);
	}

}
