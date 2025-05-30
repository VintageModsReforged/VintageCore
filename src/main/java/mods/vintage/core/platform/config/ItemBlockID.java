package mods.vintage.core.platform.config;

import mods.vintage.core.VintageCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

public class ItemBlockID {

    private final Mode mode;
    String name;
    private int id;
    private byte stage = 0;
    private Property myValue;
    boolean usedID = false;
    private static final int ITEM_SHIFT = 256;

    private ItemBlockID(Mode mode, String name, int defaultID) {
        this.mode = mode;
        this.name = name;
        this.id = defaultID;
    }

    public static ItemBlockID ofItem(String name, int defaultValue) {
        return new ItemBlockID(Mode.ITEM, name, defaultValue);
    }

    public static ItemBlockID ofBlock(String name, int defaultValue) {
        return new ItemBlockID(Mode.BLOCK, name, defaultValue);
    }

    public void init(Configuration config) {
        if (this.stage == 0) {
            if (this.mode == Mode.BLOCK) {
                this.myValue = config.get("block", this.name, -1);
            } else {
                this.myValue = config.get("item", this.name, -1);
            }

            if (this.myValue.wasRead()) {
                if (this.myValue.getInt() == -1) {
                    this.id = -1;
                    this.stage = 2;
                } else {
                    if (this.mode == Mode.BLOCK) {
                        this.myValue = config.getBlock(this.name, this.id);
                    } else {
                        this.myValue = config.getItem(this.name, this.id);
                    }
                    this.myValue.comment = "Default: " + this.id;
                    this.id = this.myValue.getInt();
                    this.stage = 2;
                }
            } else {
                this.stage = 1;
            }
        } else {
            throw new RuntimeException("Invalid Configuration!");
        }
    }

    public void confirm(Configuration config) {
        if (this.stage != 1 && this.id != -1) {
            if (this.stage != 2) {
                throw new RuntimeException("Invalid Configuration!");
            }
        } else if (this.id > 0) {
            if (this.mode == Mode.BLOCK) {
                this.myValue = config.getBlock(this.name, this.id);
            } else {
                this.myValue = config.getItem(this.name, this.id);
            }
            this.myValue.comment = "Default: " + this.id;
            this.id = this.myValue.getInt();
            VintageCore.LOGGER.info("Automatically took " + this.mode + "ID " + this.id + " for " + this.name);
        }
    }

    public int get() {
        this.usedID = true;
        return this.myValue.getInt();
    }

    public void setID(Object entry, int blockID) {
        int myValueInt = this.myValue.getInt();
        if (blockID != myValueInt) {
            VintageCore.LOGGER.fine("Strange ID Behavior detected with " + entry.getClass().getName() + ", changing to new ID: " + blockID + ", old ID: " + myValueInt);
            this.myValue.set(blockID);
            this.id = blockID;
        }
    }

    public void setItemID(Object entry, int itemID) {
        this.setID(entry, itemID - ITEM_SHIFT);
    }

    public void confirmOwnership(String modid) {
        if (this.usedID) {
            if (this.id > 0) {
                String what = null;
                if (this.mode == Mode.BLOCK) {
                    Block existing = Block.blocksList[this.id];
                    if (existing != null) {
                        if (existing instanceof IItemBlockIDProvider) {
                            return;
                        }
                        what = existing.getClass().getName();
                    }
                } else if (Item.itemsList[this.id + ITEM_SHIFT] != null) {
                    Item existing = Item.itemsList[this.id + ITEM_SHIFT];
                    if (existing != null) {
                        if (existing instanceof IItemBlockIDProvider) {
                            return;
                        }
                        what = existing.getClass().getName();
                    }
                }

                String separator = "\n\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n";
                if (what != null) {
                    throw new RuntimeException(separator + modid + " is configured to use " + this.mode + "ID " + this.id + " for " + this.name + "\n\nIt is currently occupied by " + what + ".\n\nEdit your configs and resolve the conflict." + separator);
                } else {
                    throw new RuntimeException(separator + modid + " is configured to use " + this.mode + "ID " + this.id + " for " + this.name + ", however something went wrong." + separator);
                }
            }
        }
    }

    public enum Mode {
        ITEM,
        BLOCK
    }
}
