package mods.vintage.core.platform.lang;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.vintage.core.VintageCore;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LangManager {

    private static final String LOCALIZATION_PROVIDER_LIST_ANNOTATION =
            "mods.vintage.core.platform.lang.LocalizationProvider$List";

    public static final LangManager INSTANCE = new LangManager();
    List<ASMDataTable.ASMData> LOCALIZATION_PROVIDERS = new ArrayList<ASMDataTable.ASMData>();

    public void loadCreativeTabName(String modid, String tabName) {
        LanguageRegistry.instance().addStringLocalization("itemGroup." + modid, tabName);
    }

    /**
     * Called during preInit.
     * <p>
     * We scan directly for @LocalizationProvider.List fields.
     * <p>
     * This deliberately uses the annotation name as a String instead of:
     * <p>
     *     LocalizationProvider.List.class.getName()
     * <p>
     * because the FML RelaunchClassLoader has trouble loading my annotation
     * class directly...
     */
    public void scanForLocalizationProviders(ASMDataTable asmDataTable) {
        LOCALIZATION_PROVIDERS.clear();
        for (ASMDataTable.ASMData data : asmDataTable.getAll(LOCALIZATION_PROVIDER_LIST_ANNOTATION)) {
            LOCALIZATION_PROVIDERS.add(data);
            VintageCore.LOGGER.info("Found localization provider: "
                    + data.getClassName() + "."
                    + data.getObjectName()
            );
        }

        // I want to log it just in case
        VintageCore.LOGGER.info("Found " + LOCALIZATION_PROVIDERS.size() + " localization provider fields");
    }

    // called during init;
    public void processLocalizationProviders() {
        for (ASMDataTable.ASMData data : LOCALIZATION_PROVIDERS) {
            String className = data.getClassName();
            String fieldName = data.getObjectName();

            try {
                /*
                 * The annotation itself should never be loaded here.
                 *
                 * FML already parsed the annotation and stored its
                 * parameters in ASMData.getAnnotationInfo().
                 */
                Map<String, Object> annotationInfo = data.getAnnotationInfo();
                if (annotationInfo == null) {
                    VintageCore.LOGGER.info("No annotation information for "
                            + className + "."
                            + fieldName
                    );
                    continue;
                }

                Object modIdObject = annotationInfo.get("modId");

                if (!(modIdObject instanceof String)) {
                    VintageCore.LOGGER.info("No valid modId found for "
                            + className + "."
                            + fieldName
                    );
                    continue;
                }

                String modId = (String) modIdObject;
                Class<?> clazz = Class.forName(className);
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(null);
                if (!(value instanceof String[])) {
                    VintageCore.LOGGER.info(
                            "Localization provider field "
                                    + className + "."
                                    + fieldName + " is not a String[]"
                    );
                    continue;
                }

                String[] languageArray = (String[]) value;
                if (languageArray.length == 0) {
                    continue;
                }

                List<String> languages = Arrays.asList(languageArray);
                registerLanguages(clazz, modId, languages);
            } catch (NoSuchFieldException e) {
                FMLLog.severe("Could not find localization provider field "
                        + className + "."
                        + fieldName
                );

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access localization provider field "
                                + className + "."
                                + fieldName, e
                );
            } catch (ClassNotFoundException e) {
                FMLLog.severe("Could not find localization provider class "
                                + className
                );
            }
        }
        LOCALIZATION_PROVIDERS.clear();
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
            /*
             * First try to find the language file inside the provider's
             * JAR/classpath.
             * Example:
             * /mods/mymod/lang/en_US.json
             */
            stream = provider.getResourceAsStream("/mods/" + modid + "/lang/" + lang + ".json");
            if (stream == null) {
                /*
                 * If it isn't inside the JAR, try:
                 * .minecraft/config/<modid>/lang/<lang>.json
                 */
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
