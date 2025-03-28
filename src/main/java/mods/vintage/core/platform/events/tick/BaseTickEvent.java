package mods.vintage.core.platform.events.tick;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

import java.util.EnumSet;

public class BaseTickEvent implements ITickHandler {

    TickType TYPE;
    String ID;

    public BaseTickEvent(TickType type, String modid) {
        this.TYPE = type;
        this.ID = modid;
    }

    @Override
    public void tickStart(EnumSet<TickType> enumSet, Object... objects) {}

    @Override
    public void tickEnd(EnumSet<TickType> enumSet, Object... objects) {}

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(this.TYPE);
    }

    @Override
    public String getLabel() {
        return this.ID;
    }

    public boolean shouldTick(EnumSet<TickType> type) {
        return type.contains(this.TYPE);
    }
}
