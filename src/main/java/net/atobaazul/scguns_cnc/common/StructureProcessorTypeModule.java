package net.atobaazul.scguns_cnc.common;


import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.atobaazul.scguns_cnc.world.processor.WaterloggedProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import static net.atobaazul.scguns_cnc.SCGunsCnC.MOD_ID;

/***
 * @author yungnickyoung
 */
@AutoRegister(MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("waterlogged_processor")
    public static StructureProcessorType<WaterloggedProcessor> WATERLOGGED_PROCESSOR = () -> WaterloggedProcessor.CODEC;

}