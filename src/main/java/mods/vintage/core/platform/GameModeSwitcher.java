package mods.vintage.core.platform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import org.lwjgl.input.Keyboard;

public class GameModeSwitcher {

    private static boolean isF4Pressed = false;

    public static void onKeyInput() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityClientPlayerMP player = mc.thePlayer;

        if (player == null) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_F4)) {
            isF4Pressed = true;
        } else if (isF4Pressed) {
            player.sendChatMessage("/gm");
            isF4Pressed = false;
        }
    }
}
