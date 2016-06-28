package com.greenlaw110.numpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class App {

    private static final Logger logger = LogManager.getFormatterLogger(App.class);

    public static void main(String[] args) throws Exception {
        benchmark();
        //tc6();
        //testCase1();
        //testcase2();
        //testcase3();
        //testcase4();
        //testcase5();
    }

    private static void benchmark() throws Exception {
        NumPoolConfig.configureBitSetWords(1);
        long max = Integer.MAX_VALUE * 100L;
        //long max = 10;
        NumPool pool = new NumPool(0, max);
        Random r = new Random();
        Set<Long> consumed = Collections.synchronizedSet(new TreeSet<Long>());
        long ms = System.currentTimeMillis();
        int n = 0, m = 0;
        for (int i = 0; i < 1000 * 1000; ++i) {
            long l = r.nextLong();
            l = Math.abs(l % max);
            try {
                if (!consumed.contains(l)) {
                    //System.err.printf("-%d::%s::%s\n", l, pool.toString("|"), consumed);
                    //System.err.printf("-%d::%s\n", l, pool.toString("|"));
                    //logger.trace("-%d::%s::%s", l, pool.blockCount(), consumed.size());
                    pool.checkOut(l);
                    consumed.add(l);
                    n++;
                } else {
                    //System.err.printf("+%d::%s::%s\n", l, pool.toString("|"), consumed);
                    //System.err.printf("+%d::%s\n", l, pool.toString("|"));
                    //logger.trace("-%d::%s::%s", l, pool.blockCount(), consumed.size());
                    pool.checkIn(l);
                    consumed.remove(l);
                    m++;
                }
            } catch (NumberNotAvailableException e) {
                e.printStackTrace();
                logger.error(e);
                logger.info("consumed side: %s", consumed.size());
                logger.info("pool: \n%s", pool.toString("\n"));
                ms = System.currentTimeMillis() - ms;
                System.out.printf("it takes %sms to finish %s times take and %s times offer\n", ms, n, m);
                Runtime runtime = Runtime.getRuntime();
                System.out.printf("memory used: %dMB, free: %dMB\n", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024), runtime.freeMemory() / (1024 * 1024));
                return;
            }
        }
        //System.err.printf("%s::%s\n", pool.toString("\n"), consumed);
        System.out.printf("#block of pool: %s\n", pool.blockCount());
        System.out.printf("consumed size: %s\n", consumed.size());
        //logger.info("pool: \n%s\n\nconsumed: \n%s", pool.toString("\n"), consumed.stream().map(Object::toString).collect(Collectors.joining("\n")));
        ms = System.currentTimeMillis() - ms;
        System.out.printf("it takes %sms to finish %s times take and %s times offer\n", ms, n, m);
        Runtime runtime = Runtime.getRuntime();
        System.out.printf("memory used: %dMB, free: %dMB\n", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024), runtime.freeMemory() / (1024 * 1024));
        consumed.clear();
        Thread.sleep(1000);
        System.out.printf("memory used | after clear consumed: %dMB, free: %dMB\n", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024), runtime.freeMemory() / (1024 * 1024));
        System.gc();
        Thread.sleep(1000);
        System.out.printf("memory used | after gc: %dMB, free: %dMB\n", (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024), runtime.freeMemory() / (1024 * 1024));
        ms = System.currentTimeMillis();
        pool.toString();
        System.out.printf("pool toString takes: %sms\n", System.currentTimeMillis() - ms);
    }

    private static void testCase1() {
        long max = 100;
        NumPool pool = new NumPool(0, max);
        long[] la = {63, 77, 92, 12, 52, 27, 88, 72, 32, 14, 74, 21, 78, 9, 31, 95, 25, 58, 91, 5, 76, 35, 43, 3, 37, 19, 83, 8, 11, 62, 51, 13, 89, 22, 16, 96, 81, 39, 70, 57, 41, 30, 53, 10, 69, 73, 29, 18, 87, 85, 38, 55, 98, 36, 86, 67, 6};
        for (int i = 0; i < la.length; ++i) {
            pool.checkOut(la[i]);
        }
        long[] la0 = {3, 5, 6, 8, 9, 10, 11, 12, 13, 14, 16, 18, 19, 21, 22, 25, 27, 29, 30, 31, 32, 35, 36, 37, 38, 39, 41, 43, 51, 52, 53, 55, 57, 58, 62, 63, 67, 69, 70, 72, 73, 74, 76, 77, 78, 81, 83, 85, 86, 87, 88, 89, 91, 92, 95, 96, 98};
        for (int i = 0; i < la.length; ++i) {
            pool.checkIn(la0[i]);
        }

        System.out.println(pool);
    }

    private static void testcase2() {
        NumPool pool = new NumPool(0, 10);
        pool.checkOut(1);
        pool.checkOut(3);
        pool.checkOut(7);
        pool.checkIn(1);
        System.out.println(pool);
    }

    private static void testcase3() {
        String[] scripts = {"-0", "-3", "-5", "-9", "-6", "-8", "+9", "+8", "-1", "+3", "-3", "+5", "+0", "+1", "-7", "-2", "-5", "-8", "-1", "+7", "-9", "+3", "-0", "+6", "+5", "-5", "-7", "+0", "-4", "-0", "-3", "+0", "+5", "+4", "-6", "+3", "-4", "+1", "-5"};
        NumPool pool = new NumPool(0, 10);
        ScriptPlayer.play(pool, scripts);
    }

    private static void testcase4() {
        String[] scripts = {"-7", "-1", "-5", "+7", "+5", "-9", "-6", "+6", "-6", "-7", "-2", "+6", "+1", "-3", "+2", "+9", "+3", "-5", "+5", "-9", "+7", "-2", "+2", "-6", "-0", "-8", "-3", "-2", "-7", "+6", "+0", "-6"};
        NumPool pool = new NumPool(0, 10);
        ScriptPlayer.play(pool, scripts);
    }

    private static void testcase5() {
        NumPoolConfig.configureBitSetWords(2);
        NumPool pool = new NumPool(0, 65);
        pool.checkOut(13);
        pool.checkOut(8);
        pool.checkOut(22);
        pool.checkOut(0);
        System.out.println(pool);
    }

    private static void tc6() {
        String[] scripts = {"-103", "-82", "-127", "-64", "-99", "-120", "-32", "-92", "-129", "-119", "-112", "-40", "-87", "-65", "+82", "-71", "-51", "-98", "+119", "-79", "-67", "-122", "-96", "+92", "-104", "-10", "-75", "-83", "-16", "-89", "-70", "-30", "-2", "-94", "-49", "+96", "-82", "+75", "-60", "+99", "-99", "+70", "+40", "-100", "+98", "-53", "-101", "-117", "-44", "-36", "-46", "+89", "-109", "-124", "-17", "+127", "-55", "-119", "+60", "-102", "+67", "+10", "-22", "-114", "-111", "-69", "+87", "-35", "+129", "+65", "-34", "-87", "-57", "-5", "+69", "-91", "-106", "-80", "-77", "+5", "-70", "+44", "+117", "+102"};
        NumPoolConfig.configureBitSetWords(2);
        NumPool pool = new NumPool(0, 130);
        ScriptPlayer.play(pool, scripts);
    }
}
