package com.ty.itadachang;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Main {



    public static void main(String[] args) {
        Set<List> set=new HashSet<>();
        List<Integer> l1 = Arrays.asList(1, 2, 3);
        List<Integer> l2 = Arrays.asList(4, 5,6);
        set.add(l1);
        set.add(l2);
        set.forEach(System.out::println);

    }

    private static void m1() {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.park();
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t"  + "---被叫醒");
        }, "t1");
        t1.start();

        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t"  + "---发出通知");
        }, "t2").start();

        try {   TimeUnit.SECONDS.sleep(2);   } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t"  + "---发出通知");
        }, "t3").start();
    }

}
