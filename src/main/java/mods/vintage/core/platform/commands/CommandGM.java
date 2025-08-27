package mods.vintage.core.platform.commands;

import mods.vintage.core.platform.lang.Translator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.EnumGameType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandGM extends CommandBase {

    public static Map<EnumGameType, String> MAPPED_MODES = new HashMap<EnumGameType, String>();
    static {
        MAPPED_MODES.put(EnumGameType.SURVIVAL, "mode.survival.name");
        MAPPED_MODES.put(EnumGameType.CREATIVE, "mode.creative.name");
    }

    @Override
    public String getCommandName() {
        return "gm";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "/gm";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(iCommandSender);
        EnumGameType mode = playerMP.capabilities.isCreativeMode ? EnumGameType.SURVIVAL : EnumGameType.CREATIVE;
        playerMP.addChatMessage(Translator.GRAY.format("chat.gamemode.switched", Translator.GOLD.format(MAPPED_MODES.get(mode))));
        playerMP.setGameType(mode);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return o instanceof ICommand ? this.getCommandName().compareTo(((ICommand) o).getCommandName()) : 0;
    }
}
