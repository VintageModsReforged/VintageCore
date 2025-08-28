package mods.vintage.core.utils;

/**
 * A simple generic container class that holds two related objects, also known as a 2-tuple or pair.
 * <p>
 * Tuples are useful for returning multiple values from a method or grouping values together
 * without creating a dedicated class. Each element can be of a different type.
 *
 * @param <FIRST>  the type of the first element in the tuple
 * @param <SECOND> the type of the second element in the tuple
 */
public class Tuple<FIRST, SECOND> {

    /** The first element of the tuple. */
    private FIRST first;

    /** The second element of the tuple. */
    private SECOND second;

    /**
     * Constructs a new tuple with the given values.
     *
     * @param first  the first element of the tuple
     * @param second the second element of the tuple
     */
    public Tuple(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first element of the tuple.
     *
     * @return the first element
     */
    public FIRST getFirst() {
        return this.first;
    }

    /**
     * Returns the second element of the tuple.
     *
     * @return the second element
     */
    public SECOND getSecond() {
        return second;
    }

    /**
     * Updates the first element of the tuple.
     *
     * @param first the new value for the first element
     */
    public void setFirst(FIRST first) {
        this.first = first;
    }

    /**
     * Updates the second element of the tuple.
     *
     * @param second the new value for the second element
     */
    public void setSecond(SECOND second) {
        this.second = second;
    }
}
