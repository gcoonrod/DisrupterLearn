package com.ca.platform.learn.disrupter.test;

import com.ca.platform.learn.disrupter.Consumer;
import com.ca.platform.learn.disrupter.Event;
import com.ca.platform.learn.disrupter.Producer;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by coonrod on 6/9/14.
 */
@RunWith(Theories.class)
public class EntryCountTest {
    private ExecutorService executor;
    private Disruptor<Event> disruptor;
    private long startTime;
    private long endTime;

    @Before
    public void setup(){
        executor = Executors.newCachedThreadPool();
    }

    public static @DataPoints long[] points = {100l, 1000l, 10000l, 100000l, 1000000l, 10000000l, 9223372036854775807l};

    @Theory
    public void varyCount(long points) throws Exception {
        startTime = System.nanoTime();
        System.out.println("Beginning tests: " + points);

        disruptor = new Disruptor<Event>(Event.factory, 1024, executor, ProducerType.MULTI, new BusySpinWaitStrategy());
        disruptor.handleEventsWith(new Consumer("C1")).then(new Consumer("C2")).then(new Consumer("C3"));

        Producer producer1 = new Producer("P1", points, disruptor).goSlow();
        Producer producer2 = new Producer("P2", points, disruptor);
        //Producer producer3 = new Producer("P3", points, disruptor);
        disruptor.publishEvent(producer1);
        disruptor.publishEvent(producer2);
        //disruptor.publishEvent(producer3);
        disruptor.start();
        executor.submit(producer1);
        executor.submit(producer2);
        //executor.submit(producer3);

        sleep();
        producer1.stop();
        producer2.stop();
        //producer3.stop();

        endTime = System.nanoTime();
        System.out.println("Tests complete");

        long duration = endTime - startTime;
        double seconds = (double)duration / 1000000000.0;
        System.out.println("Duration: " + seconds + "s");
    }

    @After
    public void shutDown(){
        disruptor.shutdown();
        executor.shutdown();
    }

    private void sleep() throws InterruptedException {Thread.sleep(3000);}
}
