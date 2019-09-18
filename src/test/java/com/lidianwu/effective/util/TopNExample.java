package com.lidianwu.effective.util;

import java.util.Comparator;
import java.util.Random;

/**
 * TopNExample
 *
 * @author Created by ldianwu on 2019/9/18
 */
public class TopNExample {

    public static void main(String[] args) {
        TopN<Integer, String> topN = new TopN<Integer, String>(10, new Comparator<Integer>() {
            // 第一个参数小于、等于、大于第二个参数
            // 对应：-1、0、1
            @Override
            public int compare(Integer x, Integer y) {
                return (x < y) ? -1 : 1;
            }
        });

        // 商品分类
        String[] categories = {
                "a",
                "b",
                "c",
                "d",
                "e",
                "f",
                "g",
        };

        Random r = new Random();
        // 随机商品数
        for (int i = 0; i < 100000000; i++) {
            topN.put(r.nextInt(1000), categories[r.nextInt(categories.length - 1)]);
        }

        // 打印
        System.out.println(topN.getTreeMap().descendingMap());
    }
}
