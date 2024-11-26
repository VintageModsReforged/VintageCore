package mods.vintage.core.platform.lang;

import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.vintage.core.VintageCore;
import net.minecraft.client.Minecraft;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

public class LangManager {

    public static final LangManager THIS = new LangManager();

    public <T extends ILangProvider> void registerLangProvider(T provider) {
        List<String> languages = provider.getLocalizationList();
        String modid = provider.getModid();
        if (!languages.isEmpty()) {
            for (String lang : languages) {
                addEntry(provider, modid, lang);
            }
            LanguageRegistry.reloadLanguageTable();
        }
    }

    private static <T extends ILangProvider> void addEntry(T provider, String modid, String lang) {
        InputStream stream = null;
        InputStreamReader reader = null;
        boolean langPresent = false;
        String langFound = "";
        try {
            VintageCore.LOGGER.info(String.format("Trying to load %1$s.lang file from /mods/%2$s/lang folder...", lang, modid));
            stream = provider.getClass().getResourceAsStream("/mods/" + modid + "/lang/" + lang + ".lang"); // use the default .lang file from modJar
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
