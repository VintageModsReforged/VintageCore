package mods.vintage.core.platform.events;

import cpw.mods.fml.common.TickType;
import mods.vintage.core.Refs;
import mods.vintage.core.platform.GameModeSwitcher;
import mods.vintage.core.platform.events.tick.TickEvents;

import java.util.EnumSet;

public class ClientTickEvent extends TickEvents.ClientTickEvent {

    public ClientTickEvent() {
        super(Refs.ID);
    }

    @Override
    public void tickStart(EnumSet<TickType> enumSet, Object... objects) {
        if (shouldTick(enumSet)) {
            GameModeSwitcher.onKeyInput();
        }
    }
}
