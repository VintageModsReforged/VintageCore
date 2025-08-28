package mods.vintage.core.utils.function;

/**
 * Represents a predicate (boolean-valued function) of one argument.
 *
 * <p>This is an implementation of functional interface from Java 1.8 and above
 * whose functional method is {@link #test(Object)}.
 *
 * @param <T> the type of the input to the predicate
 *
 */
public interface Predicate<T> {

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param t the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    boolean test(T t);
}
