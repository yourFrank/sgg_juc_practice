package com.ty.itadachang.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock {

    public static void main(String[] args) {

         Object obj1 =new Object();
         Object obj2 =new Object();
         Lock lock=new ReentrantLock();
         Lock lock2=new ReentrantLock();

         new Thread(() -> {
             synchronized (obj1){
                 System.out.println(Thread.currentThread().getName() +"持有了obj 锁");
                 try {   TimeUnit.SECONDS.sleep(1);   } catch (InterruptedException e) { e.printStackTrace(); }
                 synchronized (obj2){
                     System.out.println(Thread.currentThread().getName() +"\t 我进来了");
                 }
             }
         }, "t1").start();


        new Thread(() -> {
             synchronized (obj2){
                 System.out.println(Thread.currentThread().getName() +"持有了obj2 锁");
                 try {   TimeUnit.SECONDS.sleep(1);   } catch (InterruptedException e) { e.printStackTrace(); }
                synchronized (obj1){
                    System.out.println(Thread.currentThread().getName() +"\t 我进来了");
                }
            }

         }, "t2").start();
    }
}
