package com.ty.threadtest;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 注意这个资源类不要实现Runnable 也不要继承Thread，保证类的干净，做到高内聚低耦合（这才是企业级，实现接口那都是平时练习用的demo）
 */
class Ticket {

    private int number = 30;
    private Lock lock = new ReentrantLock();

    //使用synchronized
    public synchronized void saleTicket() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + "\t卖出第" + (number--) + "\t 还剩下：" + number);
        }
    }

    //使用lock锁功能更强大
    public  void saleTicket2() {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t卖出第" + (number--) + "\t 还剩下：" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public int getNumber() {
        return number;
    }
}

/**
 * 线程      操作      资源类
 */
public class SaleTicket {
    public static void main(String[] args) {
        //创建出线程要操作的资源类
        Ticket ticket = new Ticket();
//        Ticket ticket2 = new Ticket();
        //企业中要使用 public Thread(Runnable target, String name)  这个构造函数，创建匿名内部类，在内部类里面调用资源类方法
        //不要再实现Runnable接口了
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.saleTicket();
            }
        }, "t1").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.saleTicket();
            }
        }, "t2").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.saleTicket();
            }
        }, "t3").start();

        System.out.println();

    }


}
