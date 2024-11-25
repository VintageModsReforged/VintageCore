package mods.vintage.core.platform.lang;

import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.vintage.core.VintageCore;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LangManager {

    public static final LangManager THIS = new LangManager();

    public <T extends ILangProvider> void registerLangProvider(T provider) {
        List<String> languages = new ArrayList<String>();
        languages.add("en_US"); // default language
        String modid = provider.getModid();
        try {
            BufferedReader langFile = new BufferedReader(new InputStreamReader(Minecraft.class.getResourceAsStream("/lang/languages.txt"), "UTF-8"));

            for (String langEntry = langFile.readLine(); langEntry != null; langEntry = langFile.readLine()) {
                String[] langPair = langEntry.split("=");
                if (langPair != null && langPair.length == 2) {
                    languages.add(langPair[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
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
        try {
            VintageCore.LOGGER.info("Trying to load " + lang + ".lang file from /mods/" + modid + "/lang/ folder...");
            stream = provider.getClass().getResourceAsStream("/mods/" + modid + "/lang/" + lang + ".lang"); // use the default .lang file from modJar
            if (stream == null) {
                VintageCore.LOGGER.info("Trying to load " + lang + ".lang file from /config/" + modid + "/lang/ folder...");
                stream = new FileInputStream(Minecraft.getMinecraftDir() + "/config/" + modid + "/lang/" + lang + ".lang"); // use the lang files from config/modid/lang folder
            }
            if (stream == null) {
                VintageCore.LOGGER.info("Couldn't load " + lang + ".lang file for " + modid + "...");
            }
            reader = new InputStreamReader(stream, "UTF-8");
            Properties props = new Properties();
            props.load(reader);
            for (String key : props.stringPropertyNames()) {
                LanguageRegistry.instance().addStringLocalization(key, lang, props.getProperty(key));
            }

        } catch (Throwable t) {
            VintageCore.LOGGER.info("Couldn't load " + lang + ".lang file for" + modid + ". Skipping...");
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
