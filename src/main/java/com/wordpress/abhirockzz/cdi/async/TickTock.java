package com.wordpress.abhirockzz.cdi.async;

public class TickTock {
    private String tick;
    private String tock;

    public TickTock(String tick, String tock) {
        this.tick = tick;
        this.tock = tock;
    }

    public String getTick() {
        return tick;
    }

    public String getTock() {
        return tock;
    }

    @Override
    public String toString() {
        return "TickTock{" + "tick=" + tick + ", tock=" + tock + '}';
    }
    
    
}
