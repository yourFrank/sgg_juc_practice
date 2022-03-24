package com.ty.interview2.test_volatile;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 资源类
 */
class MyCache {

    private volatile Map<String, Object> map = new HashMap<>();

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    //写
    public void put(String key, Object object) {
        rwLock.writeLock().lock();

        System.out.println(Thread.currentThread().getName() + "\t 正在写入：" + key);

        try {
            //模拟网络延迟，0.3 秒
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key, object);
            System.out.println(Thread.currentThread().getName() + "\t 写入完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放锁
            rwLock.writeLock().unlock();
        }
    }

    //读
    public void get(String key) {

        rwLock.readLock().lock();
        System.out.println(Thread.currentThread().getName() + "\t 正在读取:");
        try {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object value = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t 读取完成" + value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //读锁释放
            rwLock.readLock().unlock();
        }
    }


}

//读写锁，读可以并发读，写只能一个写

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 0; i < 5; i++) {
            final int tempI=i;
            new Thread(() -> {
                // lambda表达式内部必须是final
                myCache.put(tempI+"",tempI+"");
            }, String.valueOf(i)).start();
        }


        // 线程操作资源类， 5个线程读
        for (int i = 0; i < 5; i++) {
            // lambda表达式内部必须是final
            final int tempInt = i;
            new Thread(() -> {
                myCache.get(tempInt + "");
            }, String.valueOf(i)).start();
        }

    }

}
