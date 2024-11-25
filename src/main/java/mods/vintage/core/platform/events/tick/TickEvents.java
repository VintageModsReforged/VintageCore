package mods.vintage.core.platform.events.tick;

import cpw.mods.fml.common.TickType;

public class TickEvents {

    public static class WorldTickEvent extends BaseTickEvent {
        public WorldTickEvent(String modid) {
            super(TickType.WORLD, modid);
        }
    }

    public static class RenderTickEvent extends BaseTickEvent {
        public RenderTickEvent(String modid) {
            super(TickType.RENDER, modid);
        }
    }

    public static class WorldLoadTickEvent extends BaseTickEvent {
        public WorldLoadTickEvent(String modid) {
            super(TickType.WORLDLOAD, modid);
        }
    }

    public static class ClientTickEvent extends BaseTickEvent {
        public ClientTickEvent(String modid) {
            super(TickType.CLIENT, modid);
        }
    }

    public static class ServerTickEvent extends BaseTickEvent {
        public ServerTickEvent(String modid) {
            super(TickType.SERVER, modid);
        }
    }

    public static class PlayerTickEvent extends BaseTickEvent {
        public PlayerTickEvent(String modid) {
            super(TickType.PLAYER, modid);
        }
    }
}
