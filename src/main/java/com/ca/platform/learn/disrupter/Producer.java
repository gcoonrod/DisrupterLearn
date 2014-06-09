package com.ca.platform.learn.disrupter;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.UUID;

/**
 * Created by coonrod on 6/9/14.
 */
public class Producer implements EventTranslator<Event>, Runnable {

    private Disruptor disruptor;
    private String name;
    private long count;
    private boolean stop;
    private boolean slowDown;

    public Producer(String name, long count, Disruptor disruptor){
        this.name = name;
        this.count = count;
        this.disruptor = disruptor;
    }

    @Override
    public void translateTo(Event event, long sequence) {
        event.setKey(UUID.randomUUID());
        event.setValue(name + "-" + sequence + "|");
        //System.out.println(event.toString());
    }

    @Override
    public void run() {
        int i = 0;
        while (count >= i && !stop){
            disruptor.publishEvent(this);
            ++i;
            if(slowDown) try{
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
        }
    }

    public Producer stop() {stop = true; return this;}
    public Producer goSlow() {slowDown = true; return this;}
}
