package com.ty.itadachang.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


public class AtomicDemo {


    AtomicInteger atomicInteger = new AtomicInteger();

    //每次调用加1
    public void addPlusPlus() {
        atomicInteger.incrementAndGet();
    }

    //创建50个线程
    public static final int SIZE = 50;

    public static void main(String[] args) throws InterruptedException {
        AtomicDemo atomicDemo = new AtomicDemo();
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);
        //创建50个线程，每个加1000次
        for (int i = 0; i < SIZE; i++) {

            new Thread(() -> {
                try
                {
                    for (int j = 0; j < 1000; j++) {
                        atomicDemo.addPlusPlus();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();

        }
//        try {   TimeUnit.SECONDS.sleep(1);   } catch (InterruptedException e) { e.printStackTrace(); }
        countDownLatch.await();
        System.out.println(atomicDemo.atomicInteger.get());
    }
}
