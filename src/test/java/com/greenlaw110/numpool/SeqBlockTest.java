package com.greenlaw110.numpool;

/**
 * Test {@link SeqBlock} implementation
 */
public class SeqBlockTest extends BlockTestBase {
    @Override
    protected Block createBlock(long min, long max) {
        return new SeqBlock(min, max);
    }
}
