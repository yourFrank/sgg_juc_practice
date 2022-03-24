package com.ty.itadachang.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {

    AtomicReference<Thread> atomicReference=new AtomicReference<>();

    public void lock(){
        //lock的时候希望是null(上厕所没有人占用)，那么我就可以修改成自己。一直循环等待
        while (!atomicReference.compareAndSet(null,Thread.currentThread())){

        }
        System.out.println(Thread.currentThread().getName()+"\t 进来了");
    }


    public void unLock(){
        //解锁的时候就不用循环判断了，如果是自己的话就释放锁
        atomicReference.compareAndSet(Thread.currentThread(),null);
        System.out.println(Thread.currentThread().getName()+"\t 释放锁");

    }
    public static void main(String[] args) {
        SpinLockDemo spinLockDemo=new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.lock();
            //第一个线程持有锁之后等5秒释放
            try {   TimeUnit.SECONDS.sleep(5);   } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.unLock();
        }, "t1").start();
        //暂停一会儿线程，保证A线程先于B线程启动并完成
        try { TimeUnit.SECONDS.sleep( 1 ); } catch (InterruptedException e) { e.printStackTrace(); }

        //第二个线程想直接获取的时候被t1占用，会等待5秒t1释放完了之后
        new Thread(() -> {
            spinLockDemo.lock();
            spinLockDemo.unLock();
        }, "t2").start();
    }

}
