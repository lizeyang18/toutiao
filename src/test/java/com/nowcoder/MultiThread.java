package com.nowcoder;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by lizeyang on 2019/5/4.
 * function:测试线程类
 */

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("T1-%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable{
    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q){
        this.q = q;
    }
    @Override
    public void run(){
        try{
            for(int i=0;i<10;++i){
                Thread.sleep(1000);
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumerer implements Runnable{
    private BlockingQueue<String> q;
    public Consumerer(BlockingQueue<String> q){
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while(true){
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

public class MultiThread {
    public static void testThread() {
//        for (int i = 0; i < 10; ++i) {
//            new MyThread(i).start();
//        }

        for (int i = 0; i < 10; ++i) {
            final int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; ++i) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T2-%d:%d", tid, i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private static Object obj = new Object();
    //内置锁
    public static void testSynchronized1(){
        synchronized (obj){
            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3-:%d",i));
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public static void testSynchronized2(){
        synchronized (obj){
            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4-:%d", i));
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    public static void testSynchronized(){
        for(int i=0;i<10;++i){
            new Thread(new Runnable() {
                //测试内置锁
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

public static void testBlockQueue() {
    BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
    new Thread(new Producer(q)).start();
    new Thread(new Consumerer(q),"Consumer1").start();
    new Thread(new Consumerer(q),"Comsumer2").start();
}

    //原子操作
    private static int counter = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void sleep(int mills){
        try{
            Thread.sleep(mills);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void testWithAtomic(){
        for(int i=0;i<10;++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for(int j=0;j<10;++j){
                        System.out.println(atomicInteger.incrementAndGet());
                    }
                }
            }).start();
        }
    }

    public static void testWithoutAtomic(){
        for(int i=0;i<10;++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for(int j=0;j<10;++j){
                        counter++;
                        System.out.println(counter);
                    }
                }
            }).start();
        }
    }

    public static void testAtomic(){
        testWithAtomic();
        testWithoutAtomic();
    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal(){
        for(int i=0;i<10;++i){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal:"+threadLocalUserIds.get());
                }
            }).start();
        }

        for(int i=0;i<10;++i){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId = finalI;
                    sleep(1000);
                    System.out.println("NonThreadLocal:"+userId);
                }
            }).start();
        }
    }

    public static void testExecutor(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i= 0;i<10;++i){
                    sleep(1000);
                    System.out.println("Execute "+i);
                }
            }
        });
        service.shutdown();
        while(!service.isTerminated()){
            sleep(1000);
            System.out.println("Wait for termination.");
        }
    }


    public static void main(String[] agrv) {
        //testThread();
//        testSynchronized();
//        testBlockQueue();
//        testAtomic();
//        testThreadLocal();
        testExecutor();
    }

}
