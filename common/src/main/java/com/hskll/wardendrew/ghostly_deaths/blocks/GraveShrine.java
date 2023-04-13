package com.hskll.wardendrew.ghostly_deaths.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GraveShrine extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public GraveShrine() {
        super(BlockBehaviour.Properties
            .of(Material.STONE)
            .sound(SoundType.STONE)
            .strength(50f, 1200f)
            .requiresCorrectToolForDrops()
            .noOcclusion()
        );

        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return box(0, 0, 0, 16, 32, 16);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder builder) {
        return Collections.singletonList(new ItemStack(Items.TOTEM_OF_UNDYING));
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

        boolean ghost = false; // get ghost state here

        if (!ghost) {
            ItemStack mainHandItem = player.getMainHandItem();

            if (mainHandItem.is(Items.DIAMOND)) {
                double punishTier = 1; // get real punish tier here

                if (punishTier > 0) {
                    punishTier--;
                    mainHandItem.shrink(1);

                    // Save punish tier here

                    // Update Player Health here

                    if (!level.isClientSide()) {
                        player.displayClientMessage(Component.literal("A fraction of your humanity returns to you!"), true);
                        level.playSound(null, blockPos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1, 1);
                    }
                    else {
                        level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1, 1, false);
                    }
                }
            }
            else {
                if (!level.isClientSide()) {
                    player.displayClientMessage(Component.literal("You feel as if a spiritual compass binds you here"), true);
                }

                // set shrine variables here
            }

            return InteractionResult.SUCCESS;
        }

        // We are a ghost

        if (level instanceof ServerLevel serverLevel) {
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(serverLevel);
            lightning.moveTo(Vec3.atBottomCenterOf(blockPos));
            lightning.setVisualOnly(true);
            serverLevel.addFreshEntity(lightning);
        }

        if (!level.isClientSide()) {
            player.displayClientMessage(Component.literal("You feel your corporeal form restored!"), true);
            level.playSound(null, blockPos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1, 1);
        }
        else {
            level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS, 1, 1, false);
        }

        // End the ghostly state

        return InteractionResult.SUCCESS;
    }
}
