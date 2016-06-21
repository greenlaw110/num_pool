package com.greenlaw110.numpool;

import java.util.Comparator;

/**
 * Implements natural ascending order comparator based on
 * {@link Block}'s {@link Block#min()} value
 */
class BlockComparator implements Comparator<Block> {

    static final BlockComparator INSTANCE = new BlockComparator();

    private BlockComparator() {}

    public int compare(Block o1, Block o2) {
        long m1 = o1.min();
        long m2 = o2.min();
        return m1 > m2 ? 1 : m1 == m2 ? 0 : -1;
    }
}
