package com.ty.itadachang.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockDownGradingDemo
{
    public static void main(String[] args)
    {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();


        new Thread(() -> {

            try {
                writeLock.lock();
                System.out.println("-------正在写入");
                readLock.lock();
                try {   TimeUnit.SECONDS.sleep(5);   } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("-------正在读取");
                try {   TimeUnit.SECONDS.sleep(5);   } catch (InterruptedException e) { e.printStackTrace(); }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
                readLock.unlock();

            }

        }, "t2").start();

        try {   TimeUnit.SECONDS.sleep(1);   } catch (InterruptedException e) { e.printStackTrace(); }
        new Thread(() -> {
            readLock.lock();;
            try {
                System.out.println(Thread.currentThread().getName()+"\t进来了");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }

        }, "t2").start();

    }
}
