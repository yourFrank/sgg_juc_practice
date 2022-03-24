package com.ty.itadachang.threadLocal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateUtils{

    public static final ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal =ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//    public static final SimpleDateFormat  simpleDateFormatThreadLocal =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date  format(String  date) throws ParseException {
        return simpleDateFormatThreadLocal.get().parse(date);
    }

}

public class SimpleDateFormatDemo {

    public static void main(String[] args) {

        for (int i = 0; i <= 6; i++) {
            new Thread(() -> {

                try {
                    System.out.println(DateUtils.format("2020-11-11 11:11:11"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }finally {
                    DateUtils.simpleDateFormatThreadLocal.remove();
                }
            }, String.valueOf(i)).start();
        }

    }
}
