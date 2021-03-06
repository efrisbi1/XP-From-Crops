package net.chef.cropxp.events;

import net.chef.cropxp.XpFromCrops;
import net.chef.cropxp.init.ConfigInit;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class HarvestEvent {
    public HarvestEvent() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) {
                return;
            }
            try {
                ServerWorld serverWorld = player.getServer().getWorld(world.getRegistryKey());
                handleHarvest(serverWorld, state, pos, player);
            }
            catch (Exception e) {
                XpFromCrops.LOGGER.error(e.getMessage());
            }
        });
    }

    public void handleHarvest(ServerWorld world, BlockState state, BlockPos pos, PlayerEntity player) {
        Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        String blockString = state.toString().replace("Block{", "").replace("}", "");
        boolean giveXp = Arrays.asList(ConfigInit.CONFIG.crops).toString().contains(blockString);
        if (giveXp && world.random.nextInt(100) + 1 <= ConfigInit.CONFIG.chance) {
            ExperienceOrbEntity.spawn(world, vec, ConfigInit.CONFIG.amount);
            XpFromCrops.LOGGER.info(ConfigInit.CONFIG.amount + " Experience added for " + state.getBlock().getTranslationKey());
        }
    }
}
