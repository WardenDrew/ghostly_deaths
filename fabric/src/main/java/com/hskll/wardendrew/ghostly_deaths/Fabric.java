package com.hskll.wardendrew.ghostly_deaths;

import com.hskll.wardendrew.ghostly_deaths.blocks.GraveShrine;
import net.fabricmc.api.ModInitializer;
import com.hskll.wardendrew.ghostly_deaths.Constants;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

public class Fabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.getLogger().info("Ghostly Deaths for FABRIC is loading.");

        ResourceLocation graveShrineResourceLocation = new ResourceLocation(Constants.MODID, "grave_shrine");
        Block graveShrineBlock = new GraveShrine();

        Registry.register(Registry.BLOCK, graveShrineResourceLocation, graveShrineBlock);
        Registry.register(Registry.ITEM, graveShrineResourceLocation, new BlockItem(graveShrineBlock, new FabricItemSettings().tab(CreativeModeTab.TAB_DECORATIONS)));

    }
}