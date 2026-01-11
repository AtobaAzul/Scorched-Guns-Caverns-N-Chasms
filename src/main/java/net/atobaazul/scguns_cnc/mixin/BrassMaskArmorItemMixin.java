package net.atobaazul.scguns_cnc.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.teamabnormals.caverns_and_chasms.core.registry.CCBlocks;
import com.teamabnormals.caverns_and_chasms.core.registry.CCSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.animatable.GeoItem;
import top.ribs.scguns.Config;
import top.ribs.scguns.item.animated.BrassMaskArmorItem;

@Mixin(BrassMaskArmorItem.class)
public abstract class BrassMaskArmorItemMixin extends ArmorItem implements GeoItem {
    public BrassMaskArmorItemMixin(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    private boolean scguns_cnc$isValidHomUnculusStructure(Level level, BlockPos pos) {
        BlockState clickedBlock = level.getBlockState(pos);
        if (!clickedBlock.is(CCBlocks.SANGUINE_BLOCK.get())) {
            return false;
        }
        if (level.getBlockState(pos.above()).is(CCBlocks.SANGUINE_BLOCK.get())) {
            return true;
        }
        return level.getBlockState(pos.below()).is(CCBlocks.SANGUINE_BLOCK.get());
    }

    private void scguns_cnc$spawnCreationEffects(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 20; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0;
            double y = pos.getY() + level.random.nextDouble() * 2.0;
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 2.0;

            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0.0, 0.0, 0.0, 0.1);
        }
        for (int i = 0; i < 15; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 1.5;
            double y = pos.getY() + level.random.nextDouble() * 1.5;
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 1.5;

            level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 1, 0.0, 0.1, 0.0, 0.05);
        }
    }


    @WrapMethod(method = "useOn", remap = false)
    private InteractionResult useOn(UseOnContext pContext, Operation<InteractionResult> original) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        boolean disableVillagers = Config.COMMON.gameplay.disableVillagerSpawning.get();

        if (player != null && player.isShiftKeyDown() && scguns_cnc$isValidHomUnculusStructure(level, pos) && !disableVillagers) {
            if (!level.isClientSide()) {
                if (!player.getAbilities().instabuild) {
                    pContext.getItemInHand().shrink(1);
                }

                BlockPos bottomPos, topPos;
                if (level.getBlockState(pos.above()).is(Blocks.CLAY)) {
                    bottomPos = pos;
                    topPos = pos.above();
                } else {
                    bottomPos = pos.below();
                    topPos = pos;
                }

                level.setBlock(bottomPos, Blocks.AIR.defaultBlockState(), 3);
                level.setBlock(topPos, Blocks.AIR.defaultBlockState(), 3);

                scguns_cnc$spawnCreationEffects((ServerLevel) level, bottomPos.above());
                level.playSound(null, bottomPos, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
                level.playSound(null, bottomPos, CCSoundEvents.ATONING_TABLE_USE.get(), SoundSource.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);
                level.playSound(null, bottomPos, CCSoundEvents.ATONING_TABLE_WHISPERS.get(), SoundSource.BLOCKS, 0.8F, 0.8F + level.random.nextFloat() * 0.4F);


                Villager villager = new Villager(EntityType.VILLAGER, level);
                villager.moveTo(bottomPos.getX() + 0.5, bottomPos.getY(), bottomPos.getZ() + 0.5, 0.0F, 0.0F);

                var villagerTypes = BuiltInRegistries.VILLAGER_TYPE.stream().toList();
                VillagerType randomType = villagerTypes.get(level.random.nextInt(villagerTypes.size()));
                villager.setVillagerData(villager.getVillagerData().setType(randomType).setProfession(VillagerProfession.NONE));

                level.addFreshEntity(villager);
                level.playSound(null, bottomPos, SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1.0F, 1.2F);
            }

            return InteractionResult.SUCCESS;

        } else {
            return original.call(pContext);

        }

    }

}
