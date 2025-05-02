package com.asterexcrisys.bfi.services;

@SuppressWarnings("unused")
public class Counter {

    private final int target;
    private int value;

    public Counter(int target) {
        this.target = target;
        value = 0;
    }

    public Counter(int target, int value) {
        this.target = target;
        this.value = value;
    }

    public int target() {
        return target;
    }

    public int value() {
        return value;
    }

    public int distance() {
        return Math.abs(target - value);
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

}