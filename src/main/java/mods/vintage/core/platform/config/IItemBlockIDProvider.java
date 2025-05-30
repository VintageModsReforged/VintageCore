package mods.vintage.core.platform.config;

/**
 * Marker interface for blocks or items that are managed by automatic ID assignment system.
 * <p>
 * Implementing this interface indicates that the class is an internal
 * part of the mod and is expected to occupy specific item or block IDs
 * as configured.
 * <p>
 * This interface is used to bypass ID conflict validation in
 * {@code confirmOwnership} checks, allowing mod developers to avoid
 * false-positive conflicts with their own registered content.
 *
 * <p><strong>Note:</strong> This interface does not define any methods and
 * serves purely as a marker (tagging) interface.</p>
 */
public interface IItemBlockIDProvider {
}
