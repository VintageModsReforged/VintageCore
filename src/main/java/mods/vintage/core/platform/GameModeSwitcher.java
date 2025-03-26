package mods.vintage.core.platform;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.world.EnumGameType;
import org.lwjgl.input.Keyboard;

public class GameModeSwitcher {

    private static boolean isF4Pressed = false;
    private static int modeIndex = 0;
    private static final EnumGameType[] MODES = {
            EnumGameType.SURVIVAL,
            EnumGameType.CREATIVE,
            EnumGameType.ADVENTURE
    };

    public static void onKeyInput() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityClientPlayerMP player = mc.thePlayer;

        if (player == null) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_F4)) {
            if (!isF4Pressed) {
                modeIndex = (modeIndex + 1) % MODES.length;
            }
            isF4Pressed = true;
        } else if (isF4Pressed) {
            mc.playerController.setGameType(MODES[modeIndex]);
            player.addChatMessage(FormattedTranslator.GOLD.format("chat.gamemode.switched", FormattedTranslator.AQUA.literal(MODES[modeIndex].getName())));
            isF4Pressed = false;
        }
    }
}
