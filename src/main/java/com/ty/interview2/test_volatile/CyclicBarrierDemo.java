package com.ty.interview2.test_volatile;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//和CountDownLatch相反，需要集齐七颗龙珠，召唤神龙。也就是做加法，开始是0，加到某个值的时候就执行
//所有先到的线程都要等
public class CyclicBarrierDemo {


    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(7,()->{
            System.out.println("召唤神龙");
        });
        for (int i = 0; i <= 6; i++) {
            final Integer tempInt = i;
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName()+"\t 收集到第"+tempInt+"颗龙珠");
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }, String.valueOf(i)).start();
        }
    }
}
