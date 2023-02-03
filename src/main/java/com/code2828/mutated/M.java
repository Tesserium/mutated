package com.code2828.mutated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class M implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("mutated");
	protected static final SimpleDefaultedRegistry<Mutation> MUTATION = FabricRegistryBuilder
			.createDefaulted(Mutation.class, new Identifier("mutated", "mutation"),
					new Identifier("mutated", "experience_boost"))
			.attribute(RegistryAttribute.SYNCED).buildAndRegister();
	protected static final MutPotionItem POTION_OF_MUTATION = new MutPotionItem();
	protected static final Item WEIRDNESS = new Item(new FabricItemSettings().maxCount(37));

	@Override
	public void onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
			content.add(WEIRDNESS);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(content -> {
			content.add(POTION_OF_MUTATION);
		});
		Registry.register(MUTATION, new Identifier("mutated", "experience_boost"), Mutations.EXPERIENCE_BOOST);
		Registry.register(MUTATION, new Identifier("mutated", "experience_depletion"), Mutations.EXPERIENCE_DEPLETION);
		Registry.register(MUTATION, new Identifier("mutated", "fall_vulnerability"), Mutations.FALL_VULNERABILITY);
		Registry.register(Registries.ITEM, new Identifier("mutated", "potion"), POTION_OF_MUTATION);
		Registry.register(Registries.ITEM, new Identifier("mutated", "weirdness"), WEIRDNESS);
	}

}
