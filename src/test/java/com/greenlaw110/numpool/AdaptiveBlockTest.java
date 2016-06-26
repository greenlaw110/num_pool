package com.greenlaw110.numpool;

import org.junit.Test;

/**
 * Test {@link AdaptiveBlock} implementation
 */
public class AdaptiveBlockTest extends BlockTestBase {
    @Override
    protected Block createBlock(long min, long max) {
        return new AdaptiveBlock(min, max);
    }

    @Test
    public void testSplitByTake() {
        target = createBlock(112, 112 + 150);
        BlockPair pair = target.take(136);
        notNull(pair);
        eq("112-135", pair.a().toString());
        eq("137-262", pair.b().toString());
    }

    @Test
    public void testSplitByOffer() {
        target = createBlock(112, 112 + 63);
        BlockPair pair = target.take(136);
        assertNull(pair);
        pair = target.offer(111);
        notNull(pair);
        eq("111", pair.a().toString());
        eq(target, pair.b());
        pair = target.offer(176);
        notNull(pair);
        eq(target, pair.a());
        eq("176", pair.b().toString());
    }

    @Test
    public void testExpandBitSetWithoutSplit() {
        target = createBlock(112, 120);
        target.take(116); // make it a bitset block
        BlockPair pair = target.offer(111);
        assertNull(pair);
        eq("111-115,117-120", target.toString());
        pair = target.offer(121);
        assertNull(pair);
        eq("111-115,117-121", target.toString());
    }

    @Test
    public void testShrinkBitSet() {
        target = createBlock(112, 120);
        BlockPair pair = target.take(116); // make it a bitset block
        assertNull(pair);
        pair = target.take(112);
        assertNull(pair);
        eq("113-115,117-120", target.toString());
        pair = target.take(120);
        assertNull(pair);
        eq("113-115,117-119", target.toString());
    }

    @Test
    public void testEmptyBitSet() {
        target = createBlock(112, 116);
        target.take(113);
        target.take(112);
        target.take(115);
        target.take(116);
        target.take(114);
        yes(target.isEmpty());
    }
}
