package mods.vintage.core.platform.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a mod class as a provider of localization resources.
 * <p>
 * Mod authors can use this annotation to declare the languages their mod supports
 * without implementing any interfaces or calling registration code manually.
 * The localization loader automatically scans all annotated classes during the
 * {@code FMLPreInitializationEvent} phase and registers the corresponding language files.
 *
 * <h2>Usage</h2>
 * <p>
 * 1. Annotate the main mod class (or any class in your mod JAR) with {@code @LocalizationProvider}.<br>
 * 2. Create a static {@code String[]} field containing your supported languages.<br>
 * 3. Annotate that field with {@link LocalizationProvider.List}, specifying your mod ID.
 *
 * <h2>Example</h2>
 * <pre>{@code
 * @LocalizationProvider
 * @Mod(modid = "examplemod", name = "Example Mod", version = "1.0")
 * public class ExampleMod {
 *
 *     // Static field containing supported languages
 *     @LocalizationProvider.List(modId = "examplemod")
 *     public static String[] LANGS = { "en_US", "de_DE", "fr_FR" };
 * }
 * }</pre>
 *
 * <h2>Notes for Mod Authors</h2>
 * <ul>
 *     <li>The static language field must be initialized during {@code FMLPreInitializationEvent}, because the processor
 *         scans for annotated classes and fields during preInit.</li>
 *     <li>The annotation processor will load language files from:
 *         <ul>
 *             <li>{@code /mods/{modId}/lang/{lang}.lang} inside the mod JAR</li>
 *             <li>{@code /config/{modId}/lang/{lang}.lang} in the user's config folder</li>
 *         </ul>
 *     </li>
 *     <li>No need to call any registration code;</li>
 *     <li>To add new languages, simply update the static array via config or using hardcoded values and provide the corresponding `.lang` files.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LocalizationProvider {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface List {
        String modId();
    }
}
