package com.ca.platform.learn.disrupter;

import com.lmax.disruptor.EventFactory;

import java.util.UUID;

/**
 * Created by coonrod on 6/9/14.
 */
public class Event {

    private UUID key;
    private String value;

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void add(String suffix) {
        this.value += suffix;
    }

    public final static EventFactory<Event> factory = new EventFactory<Event>() {
        @Override
        public Event newInstance() {
            return new Event();
        }
    };

    @Override
    public String toString(){
        return "{" + this.key + ": " + this.value + "}";
    }
}
