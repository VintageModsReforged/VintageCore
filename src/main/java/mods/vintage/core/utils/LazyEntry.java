package mods.vintage.core.utils;

import mods.vintage.core.utils.function.Supplier;

public class LazyEntry<T> {

    private final Supplier<T> factory;
    private T instance;

    public LazyEntry(Supplier<T> factory) {
        this.factory = factory;
    }

    public T get() {
        if (this.instance == null) {
            this.instance = this.factory.get();
        }
        return this.instance;
    }
}
