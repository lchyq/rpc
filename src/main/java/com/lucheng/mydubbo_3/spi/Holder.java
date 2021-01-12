package com.lucheng.mydubbo_3.spi;

/**
 * holder ： help class hold a value
 */
public class Holder<T> {
    private T instance;

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }
}
