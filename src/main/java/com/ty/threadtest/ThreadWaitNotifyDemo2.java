package com.ty.threadtest;


class Resource {  //资源类
    private int num = 1;
    private char aChar = 'A';
    private boolean flag = true; //用来控制两个线程交替打印的变量

    public synchronized void addNum() throws InterruptedException {
        //1 、判断, 什么时候要等待？
        while (!flag) {
            this.wait();
        }
        //2、干活

        System.out.println(Thread.currentThread().getName() + "\t " + (num++) + "");
        System.out.println(Thread.currentThread().getName() + "\t " + (num++) + "");

        //3、通知
        flag = false;
        this.notifyAll();
    }

    public synchronized void addChar() throws InterruptedException {
        while (flag) {  //等待生产者去生产
            this.wait();
        }

        System.out.println(Thread.currentThread().getName() + "\t " + (char) (aChar++) + "");
        flag = true;
        this.notifyAll();
    }
}

/**
 * 两个线程，一个线程打印1-52，另一个打印字母A-Z打印顺序为12A34B...5152Z，要求用线程间通信
 */

public class ThreadWaitNotifyDemo2 {


    public static void main(String[] args) {
        Resource resouce = new Resource();
        new Thread(() -> {
            for (int i = 0; i < 26; i++) {
                try {
                    resouce.addNum();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 26; i++) {
                try {
                    resouce.addChar();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "B").start();

    }
}
