package com.ty.itadachang.completablefuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureDemo_2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            System.out.println("futuretask come in ----");
            TimeUnit.SECONDS.sleep(3);
            return 1024;
        });

        Thread t1=new Thread(futureTask,"t1");
        t1.start();

        //使用轮询的方式
        while(true)
        {
            if (futureTask.isDone())
            {
                System.out.println(futureTask.get());
                break;
            }else{
                //可以在这里添加超时退出的逻辑
                System.out.println("正在执行，别催");
            }
        }
//        System.out.println(futureTask.get(2l, TimeUnit.SECONDS));
        System.out.println("我继续上课");

    }
}
