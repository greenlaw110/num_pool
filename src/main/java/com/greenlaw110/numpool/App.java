package com.greenlaw110.numpool;

import java.util.*;

public class App {

    public static void main(String[] args) {
        benchmark();
        //testCase1();
    }

    private static void benchmark() {
        long max = Integer.MAX_VALUE * 1000L;
        NumberPool pool = new NumberPool(max);
        Random r = new Random();
        Set<Long> consumed = new LinkedHashSet<Long>(100);
        long ms = System.currentTimeMillis();
        int n = 0;
        for (int i = 0; i < 1200000; ++i) {
            long l = r.nextLong();
            l = Math.abs(l % max);
            if (!consumed.contains(l)) {
                pool.consume(l);
                consumed.add(l);
                n++;
            }
        }
        ms = System.currentTimeMillis() - ms;
        System.out.printf("it takes %sms to finish %s times consume", ms, n);
    }

    private static void testCase1() {
        long max = 100;
        NumberPool pool = new NumberPool(max);
        long[] la = {63, 77, 92, 12, 52, 27, 88, 72, 32, 14, 74, 21, 78, 9, 31, 95, 25, 58, 91, 5, 76, 35, 43, 3, 37, 19, 83, 8, 11, 62, 51, 13, 89, 22, 16, 96, 81, 39, 70, 57, 41, 30, 53, 10, 69, 73, 29, 18, 87, 85, 38, 55, 98, 36, 86, 67, 6};
        for (int i = 0; i < la.length; ++i) {
            pool.consume(la[i]);
        }
        long[] la0 = {3, 5, 6, 8, 9, 10, 11, 12, 13, 14, 16, 18, 19, 21, 22, 25, 27, 29, 30, 31, 32, 35, 36, 37, 38, 39, 41, 43, 51, 52, 53, 55, 57, 58, 62, 63, 67, 69, 70, 72, 73, 74, 76, 77, 78, 81, 83, 85, 86, 87, 88, 89, 91, 92, 95, 96, 98};
        for (int i = 0; i < la.length; ++i) {
            pool.supply(la0[i]);
        }

        System.out.println(pool);
    }

}
