package mods.vintage.core.platform.lang;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.vintage.core.VintageCore;
import net.minecraft.client.Minecraft;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LangManager {

    public static final LangManager INSTANCE = new LangManager();

    public void loadCreativeTabName(String modid, String tabName) {
        LanguageRegistry.instance().addStringLocalization("itemGroup." + modid, tabName);
    }

    public void processLocalizationProviders(ASMDataTable asmData) {
        for (ASMDataTable.ASMData data : asmData.getAll(LocalizationProvider.class.getName())) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(LocalizationProvider.List.class)) {
                        field.setAccessible(true);
                        LocalizationProvider.List fieldAnn = field.getAnnotation(LocalizationProvider.List.class);
                        String modId = fieldAnn.modId();
                        Object value = field.get(null);
                        if (value instanceof String[]) {
                            List<String> languages = Arrays.asList((String[]) value);
                            if (!languages.isEmpty()) {
                                registerLanguages(clazz, modId, languages);
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to process @LocalizationProvider for class: " + data.getClassName(), e);
            } catch (ClassNotFoundException ignored) {}
        }
    }

    private void registerLanguages(Class<?> provider, String modId, List<String> languages) {
        if (!languages.isEmpty()) {
            for (String lang : languages) {
                addEntry(provider, modId, lang);
            }
            LanguageRegistry.reloadLanguageTable();
        }
    }

    private void addEntry(Class<?> provider, String modid, String lang) {
        InputStream stream = null;
        InputStreamReader reader = null;
        boolean langPresent = false;
        String langFound = "";
        try {
            VintageCore.LOGGER.info(String.format("Trying to load %1$s.lang file from /mods/%2$s/lang folder...", lang, modid));
            stream = provider.getResourceAsStream("/mods/" + modid + "/lang/" + lang + ".lang"); // use the default .lang file from modJar
            if (stream == null) {
                VintageCore.LOGGER.info(String.format("Couldn't load %1$s.lang file from mods/%2$s/lang folder for %2$s... Did you put it in config/%2$s/lang folder? Let me check...", lang, modid));
                VintageCore.LOGGER.info(String.format("Trying to load %1$s.lang file from /config/%2$s/lang folder...", lang, modid));
                stream = new FileInputStream(Minecraft.getMinecraftDir() + "/config/" + modid + "/lang/" + lang + ".lang"); // use the lang files from config/modid/lang folder
                if (stream != null) {
                    langPresent = true;
                    langFound = lang;
                }
            } else {
                langPresent = true;
                langFound = lang;
            }
            reader = new InputStreamReader(stream, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            for (String key : props.stringPropertyNames()) {
                LanguageRegistry.instance().addStringLocalization(key, lang, props.getProperty(key));
            }
            if (langPresent) {
                VintageCore.LOGGER.info(String.format("Loaded lang file for %s!", langFound));
            }

        } catch (Throwable t) {
            VintageCore.LOGGER.info(String.format("Couldn't load %s.lang file for %s. Skipping...", lang, modid));
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Throwable ignored) {}
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable ignored) {}
            }
        }
    }
}
