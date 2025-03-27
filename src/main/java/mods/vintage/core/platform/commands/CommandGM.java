package mods.vintage.core.platform.commands;

import mods.vintage.core.platform.lang.FormattedTranslator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.EnumGameType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandGM extends CommandBase {

    private static int modeIndex = 0;
    public static Map<EnumGameType, String> MAPPED_MODES = new HashMap<EnumGameType, String>();
    static {
        MAPPED_MODES.put(EnumGameType.SURVIVAL, "mode.survival.name");
        MAPPED_MODES.put(EnumGameType.CREATIVE, "mode.creative.name");
        MAPPED_MODES.put(EnumGameType.ADVENTURE, "mode.adventure.name");
    }
    private static final EnumGameType[] MODES = {
            EnumGameType.SURVIVAL,
            EnumGameType.CREATIVE,
            EnumGameType.ADVENTURE
    };

    @Override
    public String getCommandName() {
        return "gm";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(iCommandSender);

        modeIndex = (modeIndex + 1) % MODES.length;
        playerMP.setGameType(MODES[modeIndex]);
        playerMP.addChatMessage(FormattedTranslator.GRAY.format("chat.gamemode.switched", FormattedTranslator.GOLD.format(MAPPED_MODES.get(MODES[modeIndex]))));
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return o instanceof ICommand ? this.getCommandName().compareTo(((ICommand) o).getCommandName()) : 0;
    }
}
