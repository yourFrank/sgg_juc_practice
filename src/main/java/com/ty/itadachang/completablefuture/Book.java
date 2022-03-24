package com.ty.itadachang.completablefuture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Book {
    private Integer id;
    private String name;
    private double price;
    private String author;

    public static void main(String[] args) {
        Book book=new Book();
        //以前的写法
//        book.setAuthor("张三");
//        book.setId(1);
//        book.setPrice(50l);


        //加了@Accessors(chain = true) 后可以使用链式调用，这个默认为false
        book.setId(1).setName("张三").setPrice(50l);

    }
}


