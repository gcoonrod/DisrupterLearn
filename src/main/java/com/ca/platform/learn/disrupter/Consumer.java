package com.ca.platform.learn.disrupter;

import com.lmax.disruptor.EventHandler;

/**
 * Created by coonrod on 6/9/14.
 */
public class Consumer implements EventHandler<Event> {
    
    private String name;
    public Consumer(String name) {this.name = name;}

    @Override
    public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
        event.add(name + "-" + sequence + "|");
        //System.out.println(event.toString());
    }
}
