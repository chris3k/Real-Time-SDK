package com.refinitiv.eta.json.util;

import java.util.function.Supplier;

public class ObjectPool<T> {
    final UtilQueue<T> pool;
    final static int DEFAULT_INITIAL_SIZE = 16;

    public ObjectPool(boolean isConcurrent, Supplier<T> supplier) {
        this(isConcurrent, DEFAULT_INITIAL_SIZE, supplier);
    }

    public ObjectPool(boolean isConcurrent, int initialSize, Supplier<T> supplier) {
        if (isConcurrent)
            pool = new UtilQueueConcurrent<T>(initialSize, supplier);
        else {
            pool = new UtilQueue<T>(initialSize, supplier);
        }
    }

    public T get() {
        return pool.get();
    }

    public void release(T obj) {
        pool.add(obj);
    }

    public void growPool(int numOfObjects) {
        pool.growPool(numOfObjects);
    }

}
