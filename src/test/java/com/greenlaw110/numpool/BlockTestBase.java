package com.greenlaw110.numpool;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Test Block Implementation against the interface
 */
public abstract class BlockTestBase extends TestBase {

    protected Block target;

    protected long targetMin;
    protected long targetMax;
    protected Random r;

    protected abstract Block createBlock(long min, long  max);

    @Before
    public void initTestTarget() {
        NumPoolConfig.configureBitSetWords(1);
        r = new Random();
        targetMin = 13L;
        long steps = Math.abs(r.nextLong() - targetMin - 1) + 1;
        targetMax = targetMin + steps;
        target = createBlock(targetMin, targetMax);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeNumLessThanMinShallRaiseException() {
        int n = 1 + r.nextInt((int) targetMin);
        target.checkOut(n);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeNumMoreThanMaxShallRaiseException() {
        long n = targetMax + 1;
        target.checkOut(n);
    }

    @Test
    public void consumeMinShallNotRaiseException() {
        target.checkOut(targetMin);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeAlreadyConsumedMinShallRaiseException() {
        target.checkOut(targetMin);
        target.checkOut(targetMin);
    }

    @Test
    public void consumeMaxShallNotRaiseException() {
        target.checkOut(targetMax);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeAlreadyConsumedMaxShallRaiseException() {
        target.checkOut(targetMax);
        target.checkOut(targetMax);
    }

    @Test
    public void consumeNumInsideBlockShallNotRaiseException() {
        long n = targetMin + 1;
        long n2 = targetMax - 1;
        target.checkOut(n);
        if (n != n2) {
            target.checkOut(n2);
        }
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(targetMin).append("-").append(targetMax);
        eq(sb.toString(), target.toString());

        target = createBlock(112, 160);
        target.checkOut(128);
        target.checkOut(131);
        target.checkOut(132);
        target.checkOut(134);
        target.checkOut(158);
        eq("112-127,129-130,133,135-157,159-160", target.toString());
    }


}
