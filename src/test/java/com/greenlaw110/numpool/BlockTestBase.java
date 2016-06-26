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
        target.take(n);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeNumMoreThanMaxShallRaiseException() {
        long n = targetMax + 1;
        target.take(n);
    }

    @Test
    public void consumeMinShallNotRaiseException() {
        target.take(targetMin);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeAlreadyConsumedMinShallRaiseException() {
        target.take(targetMin);
        target.take(targetMin);
    }

    @Test
    public void consumeMaxShallNotRaiseException() {
        target.take(targetMax);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeAlreadyConsumedMaxShallRaiseException() {
        target.take(targetMax);
        target.take(targetMax);
    }

    @Test
    public void consumeNumInsideBlockShallNotRaiseException() {
        long n = targetMin + 1;
        long n2 = targetMax - 1;
        target.take(n);
        if (n != n2) {
            target.take(n2);
        }
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(targetMin).append("-").append(targetMax);
        eq(sb.toString(), target.toString());

        target = createBlock(112, 160);
        target.take(128);
        target.take(131);
        target.take(132);
        target.take(134);
        target.take(158);
        eq("112-127,129-130,133,135-157,159-160", target.toString());
    }


}
