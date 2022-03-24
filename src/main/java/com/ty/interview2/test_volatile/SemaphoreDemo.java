package com.ty.interview2.test_volatile;


import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

//信号量用于共享资源的互斥使用、并发线程数的控制 .抢车位
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore=new Semaphore(3,false);
        for (int i = 0; i <= 6; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+"\t 抢到车位");
                    //模拟每个车
                    try {   TimeUnit.SECONDS.sleep(5);   } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println(Thread.currentThread().getName()+"\t 离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //释放车位
                    semaphore.release();
                }


            }, String.valueOf(i)).start();
        }
    }

}
