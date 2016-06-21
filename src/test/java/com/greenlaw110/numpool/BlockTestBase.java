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
        r = new Random();
        targetMin = 13L;
        long steps = Math.abs(r.nextLong() - targetMin - 1) + 1;
        targetMax = targetMin + steps;
        target = createBlock(targetMin, targetMax);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeNumLessThanMinShallRaiseException() {
        int n = 1 + r.nextInt((int) targetMin);
        target.consume(n);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeNumMoreThanMaxShallRaiseException() {
        long n = targetMax + 1;
        target.consume(n);
    }

    @Test
    public void consumeMinShallNotRaiseException() {
        target.consume(targetMin);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeAlreadyConsumedMinShallRaiseException() {
        target.consume(targetMin);
        target.consume(targetMin);
    }

    @Test
    public void consumeMaxShallNotRaiseException() {
        target.consume(targetMax);
    }

    @Test(expected = NumberOutOfRangeException.class)
    public void consumeAlreadyConsumedMaxShallRaiseException() {
        target.consume(targetMax);
        target.consume(targetMax);
    }

    @Test
    public void consumeNumInsideBlockShallNotRaiseException() {
        long n = targetMin + 1;
        long n2 = targetMax - 1;
        target.consume(n);
        if (n != n2) {
            target.consume(n2);
        }
    }

}
