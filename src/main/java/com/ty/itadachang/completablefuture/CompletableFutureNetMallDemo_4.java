package com.ty.itadachang.completablefuture;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableFutureNetMallDemo_4 {

    static List<NetMall> list= Arrays.asList(
        new NetMall("jd"),
        new NetMall("taobao"),
        new NetMall("pdd"),
         new NetMall("dangdangwang") ,
         new NetMall("tmall")
    );


    //同步获取价格的方法
    public static List<String> findPriceSync(List<NetMall> list,String productName){
        return list.stream().map(netMall -> String.format(productName + " %s price is %.2f", netMall.getMallName(), netMall.getPrice(productName))).collect(Collectors.toList());

    }


    /**
     *异步，多箭齐发
     * List<NetMall> ----> List<CompletableFuture<String>>------> List<String>
     * @param list
     * @param productName
     * @return
     */
    public static List<String> findPriceASync(List<NetMall> list,String productName){
//        return list.stream().map(netMall -> CompletableFuture.supplyAsync(() -> String.format(productName + " %s price is %.2f", netMall.getMallName(), netMall.getPrice(productName)))).collect(Collectors.toList()).stream().map(CompletableFuture::join).collect(Collectors.toList());
        return list.stream().map(netMall -> CompletableFuture.supplyAsync(() -> String.format(productName + " %s price is %.2f", netMall.getMallName(), netMall.getPrice(productName)))).collect(Collectors.toList()).stream().map(CompletableFuture::join).collect(Collectors.toList());

    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> list1 = findPriceSync(list, "thinking in java");
        for (String element : list1) {
            System.out.println(element);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("----同步costTime: "+(endTime - startTime) +" 毫秒");

        long startTime2 = System.currentTimeMillis();
        List<String> list2 = findPriceASync(list, "thinking in java");
        for (String element : list2) {
            System.out.println(element);
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("----异步costTime: "+(endTime2 - startTime2) +" 毫秒");

    }
}

/**
 * 资源类，电商
 */
class  NetMall{

    @Getter
    private String mallName;

    public NetMall(String mallName) {
        this.mallName = mallName;
    }

    //模拟计算价格（可以从redis中取）
    public double getPrice(String bookName){
        //检索需要1秒钟
        try {   TimeUnit.SECONDS.sleep(1);   } catch (InterruptedException e) { e.printStackTrace(); }
        return ThreadLocalRandom.current().nextDouble()+ bookName.charAt(0);
    }

}
