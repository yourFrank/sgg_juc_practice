package com.ty.itadachang.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureTest_3 {
    public static void main(String[] args) {
        double v = ThreadLocalRandom.current().nextDouble() * 2;
        System.out.println("a".charAt(0)+v);


    }

    private static void joinAndGet() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        System.out.println(CompletableFuture.supplyAsync(() -> {

            return 1;
        }).whenComplete((res, ex) -> {
            if (ex == null) {
                System.out.println("result:" + res);
            }
        }).exceptionally(e -> {
            e.printStackTrace();  //有异常打印异常
            return null;
        }).join());
    }


    //测试CompletableFuture之间的异步调用
    private static void m2() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture.supplyAsync(() -> {
          try {
              //模拟这个异步线程执行操作需要花费2秒钟的时间
              TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          System.out.println(Thread.currentThread().getName() + "有返回值，不指定线程池");
          return 1;
      }).thenApply((r)->{
          return r+3;   //把上一次的结果拿过来，进行处理
      }).whenComplete((res,ex)->{
          if(ex==null){ //如果没有异常
              System.out.println(res);
          }
      }).exceptionally(e->{
          e.printStackTrace();  //有异常打印异常
          return null;
      });

        System.out.println("main ---- over");

        TimeUnit.SECONDS.sleep(5);  //主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:暂停3秒钟线程
    }



    //测试CompletableFuture的四个静态方法
    private static void m1() throws InterruptedException, ExecutionException {
        //创建一个线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<Void> c1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"无返回值，不指定线程池");
        });
        //我们说过会CompletableFuture会有future 的功能， 我们直接调用get ()方法获取返回值
        System.out.println(c1.get());
        CompletableFuture<Void> c2 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"无返回值，指定线程池");
        },threadPoolExecutor);

        System.out.println(c2.get());

        CompletableFuture<Integer> c3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "有返回值，不指定线程池");
            return 1;
        });

        System.out.println(c3.get());
        CompletableFuture<Integer> c4 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "有返回值，指定线程池");
            return 2;
        },threadPoolExecutor);
        System.out.println(c4.get());


        threadPoolExecutor.shutdown();
    }
}
