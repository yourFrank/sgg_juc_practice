package com.ty.interview2.test_volatile;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

//通过CAS操作完成自旋锁，A线程先进来调用myLock方法自己持有锁5秒，
// B随后进来发现当前有线程持有锁，不是null，所以只能通过自旋等待，直到A释放锁后B随后抢到
public class SpinLockDemo {

    // 现在的泛型装的是Thread，原子引用线程
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    /**
     * 加锁
     */
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t come in");
        //开始自旋
        while (!atomicReference.compareAndSet(null, thread)) {

        }
    }

    /**
     * 解锁
     */

    public void myUnLock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + "\t invoked myUnlock()");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        // 启动t1线程，开始操作
        new Thread(() -> {

            // 开始占有锁
            spinLockDemo.myLock();


            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 开始释放锁
            spinLockDemo.myUnLock();

        }, "t1").start();


        // 让main线程暂停1秒，使得t1线程，先执行
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 1秒后，启动t2线程，开始占用这个锁
        new Thread(() -> {

            // 开始占有锁
            spinLockDemo.myLock();
            // 开始释放锁
            spinLockDemo.myUnLock();

        }, "t2").start();

        //然后1秒后，t2线程启动，发现锁被t1占有，所有不断的执行 compareAndSet方法，来进行比较，直到t1释放锁后，也就是5秒后，t2成功获取到锁，然后释放
    }

}
