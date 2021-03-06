package com.greenlaw110.numpool;

public class ScriptPlayer {

    public static void play(NumPool pool, String[] script) {
        for (String s : script) {
            boolean take = (s.startsWith("-"));
            long l = Long.parseLong(s);
            System.out.printf("%s%d::", take ? "-" : "+", Math.abs(l));
            if (!take) {
                pool.checkIn(l);
            } else {
                pool.checkOut(Math.abs(l));
            }
            System.out.println(pool);
        }
    }

}
