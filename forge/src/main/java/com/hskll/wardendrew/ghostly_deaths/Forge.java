package com.hskll.wardendrew.ghostly_deaths;

import com.hskll.wardendrew.ghostly_deaths.blocks.GraveShrine;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import com.hskll.wardendrew.ghostly_deaths.Constants;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Constants.MODID)
public final class Forge {
    public Forge() {
        Constants.getLogger().info("Ghostly Deaths for FORGE is loading.");

        MinecraftForge.EVENT_BUS.register(this);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DeferredRegister<Block> blocksRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
        RegistryObject<Block> graveShrineRO = blocksRegister.register("grave_shrine",() -> new GraveShrine());
        blocksRegister.register(modEventBus);

        DeferredRegister<Item> itemsRegister = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
        itemsRegister.register(graveShrineRO.getId().getPath(), () -> new BlockItem(graveShrineRO.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        itemsRegister.register(modEventBus);
    }

}