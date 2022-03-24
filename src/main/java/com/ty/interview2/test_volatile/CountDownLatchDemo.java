package com.ty.interview2.test_volatile;


import java.util.concurrent.CountDownLatch;

//6个同学，让班长最后走关灯
//CountDownLatch主要有两个方法，当一个或多个线程调用await方法时，调用线程就会被阻塞。
// 其它线程调用CountDown方法会将计数器减1（调用CountDown方法的线程不会被阻塞），当计数器的值变成零时，因调用await方法被阻塞的线程会被唤醒，继续执行
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch=new CountDownLatch(6);

        for (int i = 0; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"\t 上完自习，离开教室");
                countDownLatch.countDown();

            }, String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() +"\t 班长最后关门");
    }
}
