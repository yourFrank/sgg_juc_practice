package com.ty.interview2.test_volatile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * 单例模式
 */
public class SingletonDemo {

    private static volatile SingletonDemo singletonDemo;

    private SingletonDemo() {
    }

    public static SingletonDemo getObj() {

        if (singletonDemo == null) {
            synchronized (SingletonDemo.class) {
                if (singletonDemo == null) {
                    singletonDemo = new SingletonDemo();

                }
            }

        }
        return singletonDemo;
    }


    public static void main(String[] args) {

    }





}
