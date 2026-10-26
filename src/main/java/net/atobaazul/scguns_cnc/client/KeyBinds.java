package net.atobaazul.scguns_cnc.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final KeyMapping BOOST_KEY = new KeyMapping("key.scguns_cnc.use_boost", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.categories.scguns");

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BOOST_KEY);
    }
}
