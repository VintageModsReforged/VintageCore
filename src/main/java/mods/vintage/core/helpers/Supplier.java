package mods.vintage.core.helpers;

/**
 * Represents a supplier of results.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is an implementation of functional interface from Java 1.8 and above
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 *
 */
public interface Supplier<T> {
    T get();
}
