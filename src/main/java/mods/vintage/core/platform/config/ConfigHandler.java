package mods.vintage.core.platform.config;

import net.minecraftforge.common.Configuration;

import java.lang.reflect.Field;

public class ConfigHandler {

    private final String modid;

    public ConfigHandler(String modid) {
        this.modid = modid;
    }

    /**
     * Initializes item and block IDs by scanning the given configuration class for fields of type {@link ItemBlockID}.
     * This method should be called during {@link cpw.mods.fml.common.event.FMLPreInitializationEvent} to ensure that IDs are set up before other
     * components of the mod rely on them.
     *
     * @param config The configuration instance that holds ID values.
     * @throws IllegalArgumentException If the field cannot be accessed or assigned.
     * @throws IllegalAccessException If the field access is restricted.
     */
    public void initIDs(Configuration config) {
        Class<?> configClass = config.getClass();

        for (Field field : configClass.getFields()) {
            if (field.getType().isAssignableFrom(ItemBlockID.class)) {
                try {
                    ItemBlockID autoID = (ItemBlockID) field.get(configClass);
                    autoID.init(config);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Confirms item and block IDs by scanning the given configuration class for fields of type {@link ItemBlockID}.
     * This ensures that all IDs are finalized and correctly assigned.
     * This method should be called during {@link cpw.mods.fml.common.event.FMLInitializationEvent} to validate the IDs before further usage.
     * After processing, the configuration is saved to persist the changes.
     *
     * @param config The configuration instance that holds ID values.
     * @throws IllegalArgumentException If the field cannot be accessed or assigned.
     * @throws IllegalAccessException If the field access is restricted.
     */
    public void confirmIDs(Configuration config) {
        Class<?> configClass = config.getClass();

        for (Field field : configClass.getFields()) {
            if (field.getType().isAssignableFrom(ItemBlockID.class)) {
                try {
                    ItemBlockID autoID = (ItemBlockID) field.get(configClass);
                    autoID.confirm(config);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        config.save();
    }

    /**
     * Confirms ownership of item and block IDs by scanning the given configuration class for fields of type {@link ItemBlockID}.
     * This ensures that all IDs are correctly associated with the mod and prevents conflicts with other mods.
     * This method should be called during {@link cpw.mods.fml.common.event.FMLPostInitializationEvent} to finalize ownership validation.
     *
     * @param config The configuration instance that holds ID values.
     * @throws IllegalArgumentException If the field cannot be accessed or assigned.
     * @throws IllegalAccessException If the field access is restricted.
     */
    public void confirmOwnership(Configuration config) {
        Class<?> configClass = config.getClass();

        for (Field field : configClass.getFields()) {
            if (field.getType().isAssignableFrom(ItemBlockID.class)) {
                try {
                    ItemBlockID autoID = (ItemBlockID) field.get(configClass);
                    autoID.confirmOwnership(this.modid);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
