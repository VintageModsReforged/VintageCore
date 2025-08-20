package mods.vintage.core.platform.lang;

import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.vintage.core.VintageCore;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
        try {
            stream = provider.getResourceAsStream("/mods/" + modid + "/lang/" + lang + ".json");
            if (stream == null) {
                File file = new File(Minecraft.getMinecraftDir(), "/config/" + modid + "/lang/" + lang + ".json");
                if (file.exists()) stream = new FileInputStream(file);
            }

            if (stream == null) {
                VintageCore.LOGGER.info("No JSON lang file found for " + lang + " in mod " + modid);
                return;
            }

            addJsonEntry(stream, lang);
            VintageCore.LOGGER.info("Loaded JSON lang file for " + lang + " in mod " + modid);

        } catch (Throwable t) {
            VintageCore.LOGGER.info("Failed to load JSON lang file for " + lang + " in mod " + modid);
            t.printStackTrace();
        } finally {
            if (stream != null) try { stream.close(); } catch (Throwable ignored) {}
        }
    }

    private void addJsonEntry(InputStream stream, String lang) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.equals("{") || line.equals("}")) continue;

            // Expect: "key": "value",
            if (!line.contains(":")) continue;

            int colonIndex = line.indexOf(':');
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();

            // Remove quotes and trailing comma
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
            if (value.endsWith(",")) value = value.substring(0, value.length() - 1);
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }

            // Unescape basic characters
            value = value.replace("\\\"", "\"").replace("\\n", "\n").replace("\\t", "\t");

            LanguageRegistry.instance().addStringLocalization(key, lang, value);
        }
        reader.close();
    }
}
