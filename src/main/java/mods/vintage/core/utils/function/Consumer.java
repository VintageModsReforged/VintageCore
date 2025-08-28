package mods.vintage.core.utils.function;

/**
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, {@code Consumer} is expected
 * to operate via side-effects.
 *
 * <p>This is an implementation of functional interface from Java 1.8 and above
 * whose functional method is {@link #accept(Object)}.
 *
 * @param <T> the type of the input to the operation
 *
 */
public interface Consumer<T> {
    void accept(T t);
}
