package mods.vintage.core.helpers;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ClientHelper {

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.keyCode);
    }
}
