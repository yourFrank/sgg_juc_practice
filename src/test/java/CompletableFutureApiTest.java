import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.LockSupport;

public class CompletableFutureApiTest {

    @Test
    public void testHandle() {
        {
            //当一个线程依赖另一个线程时用 handle 方法来把这两个线程串行化,
            // 异常情况：有异常也可以往下一步走，根据带的异常参数可以进一步处理
            CompletableFuture.supplyAsync(() -> {
                //暂停几秒钟线程
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("111");
                return 1024;
            }).handle((f, e) -> {
                int age = 10 / 0;
                System.out.println("222");
                return f + 1;
            }).handle((f, e) -> {
                System.out.println("333");
                return f + 1;
            }).whenCompleteAsync((v, e) -> {
                System.out.println("*****v: " + v);
            }).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });

            System.out.println("-----主线程结束，END");

            // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testThenApply() {
        //当一个线程依赖另一个线程时用 thenApply 方法来把这两个线程串行化,
        CompletableFuture.supplyAsync(() -> {
            //暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("111");
            return 1024;
        }).thenApply(f -> {
            int age = 10 / 0; // 异常情况：那步出错就停在那步。
            System.out.println("222");
            return f + 1;
        }).thenApply(f -> {
            System.out.println("333");
            return f + 1;
        }).whenCompleteAsync((v, e) -> {
            System.out.println("*****v: " + v);
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });

        System.out.println("-----主线程结束，END");

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {
        }).join());


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA -> {
        }).join());


        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").join());

    }

    @Test
    public void testApplyToEither() {
        System.out.println(CompletableFuture.supplyAsync(() -> {
            try {
                //第一个1秒钟结束
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
                    try {
//                        第二个2秒钟结束
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 2;
                }
        ), r -> {
            return r;
        }).join());
    }

    @Test
    public void test4() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
            return 20;
        });

        CompletableFuture<Integer> thenCombineResult = completableFuture1.thenCombine(completableFuture2, (x, y) -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in ");
            return x + y;
        });

        System.out.println("结果为： " + thenCombineResult.get());

        long endTime = System.currentTimeMillis();

        System.out.println("----costTime: " + (endTime - startTime) + " 毫秒");

    }

    @Test
    public void test5() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> thenCombineResult = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 1");
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 2");
            return 20;
        }), (x, y) -> {
            return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in 3");
            return 30;
        }), (a, b) -> {
            return a + b;
        });
        System.out.println("-----主线程结束，END");
        System.out.println(thenCombineResult.get());


        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompose() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        System.out.println(CompletableFuture.supplyAsync(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 1;
                })
                .thenCompose(x -> CompletableFuture.supplyAsync(() -> {
                                    try {
                                        TimeUnit.SECONDS.sleep(3);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    return 2;
                                }
                        )

                ).get());
        long endTime = System.currentTimeMillis();
        System.out.println("----costTime: " + (endTime - startTime) + " 毫秒");

    }


    @Test
    public void test6() {
        //下面这个是创建数组的3种方式
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[5]);
        //AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(5);
        //AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[]{1,2,3,4,5});

        for (int i = 0; i < atomicIntegerArray.length(); i++) {
            System.out.println(atomicIntegerArray.get(i));
        }
        System.out.println();
        System.out.println();
        System.out.println();
        int tmpInt = 0;

        tmpInt = atomicIntegerArray.getAndSet(0, 1122);
        System.out.println(tmpInt + "\t" + atomicIntegerArray.get(0));
        atomicIntegerArray.getAndIncrement(1);
        atomicIntegerArray.getAndIncrement(1);
        tmpInt = atomicIntegerArray.getAndIncrement(1);
        System.out.println(tmpInt + "\t" + atomicIntegerArray.get(1));
    }




}


