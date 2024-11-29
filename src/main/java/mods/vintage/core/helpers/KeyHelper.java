package mods.vintage.core.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class KeyHelper {

    public static void registerKeybindings(KeyBinding keyBinding) {
        Minecraft.getMinecraft().gameSettings.keyBindings = Utils.add(Minecraft.getMinecraft().gameSettings.keyBindings, keyBinding);
    }
}
