package com.ty.threadtest;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//使用synchronized
class AirConditioner {  //资源类
    private int num = 0;

    public synchronized void increment() throws InterruptedException {
        //1 、判断
        while (num != 0) { //等待消费者去消费
            this.wait();
        }
        //2、干活
        num++;
        System.out.println(Thread.currentThread().getName() + "\t " + (num) + "");
        //3、通知
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (num == 0) {  //等待生产者去生产
            this.wait();
        }
        num--;
        System.out.println(Thread.currentThread().getName() + "\t " + (num) + "");
        this.notifyAll();
    }
}

//jdk8以后使用lock
class AirConditioner2 {  //资源类
    private int num = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws InterruptedException {
        lock.lock();
        try {
            //1 、判断
            while (num != 0) { //等待消费者去消费
//                this.wait();
                condition.await();
            }
            //2、干活
            num++;
            System.out.println(Thread.currentThread().getName() + "\t " + (num) + "");

            //3、通知
//            this.notifyAll();
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            lock.unlock();

        }

    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try {

            while (num == 0) {  //等待生产者去生产
//                this.wait();
                condition.await();
            }
            num--;
            System.out.println(Thread.currentThread().getName() + "\t " + (num) + "");
//            this.notifyAll();
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}


public class ThreadWaitNotifyDemo {


    public static void main(String[] args) {
        AirConditioner2 addNum = new AirConditioner2();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    addNum.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    addNum.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "B").start();


    }
}
