package com.ty.itadachang.atomic;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

class MyCar {
    public volatile Boolean isInit = false;

    AtomicReferenceFieldUpdater<MyCar, Boolean> fieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MyCar.class, Boolean.class, "isInit");

    public void init(MyCar myCar) {
        if (fieldUpdater.compareAndSet(myCar, false, Boolean.TRUE)) {
            System.out.println(Thread.currentThread().getName() + "\t init....");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "---init.....over");
        } else {
            System.out.println(Thread.currentThread().getName() + "\t" + "------其它线程正在初始化");
        }
    }
}

public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) throws InterruptedException
    {
        MyCar myVar = new MyCar();

        for (int i = 1; i <=5; i++) {
            new Thread(() -> {
                myVar.init(myVar);
            },String.valueOf(i)).start();
        }
    }
}
