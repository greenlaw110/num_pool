package com.greenlaw110.numpool;

import java.util.BitSet;

/**
 * Stores a number block state inside a BitSet
 */
class BitSetBlock implements Block {

    private long min0;
    private long min;
    private long max;

    /*
     * min and max are the first and last bit in
     * the set respectively
     */
    private BitSet bits;

    public BitSetBlock(long min, long max) {
        if ((max - min) > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("block size overflow");
        }
        this.min0 = min;
        this.min = min;
        this.max = max;
        int len = (int)(max - min + 1);
        bits = new BitSet(len);
        bits.set(0);
        bits.set(len - 1);
    }

    public long min() {
        return min;
    }

    public long max() {
        return max;
    }

    public boolean isEmpty() {
        return min > max;
    }

    public BlockPair consume(long l) {
        if (l > max || l < min) {
            throw new NumberOutOfRangeException(l);
        }
        if (l == max) {
            max--;
            bits.set(size() - 1);
        } else if (l == min) {
            bits.set((int)(min - min0), false);
            min++;
        } else {
            int offset = offset(l);
            boolean b = bits.get(offset);
            if (!b) {
                throw new NumberNotAvailableException(l);
            }
            bits.set(offset, false);
        }
        return null;
    }

    public BlockTriple supply(long l) {
        if (l > max - 1 || l < min + 1) {
            throw new NumberOutOfRangeException(l);
        }

        int offset = offset(l);
        boolean b = bits.get(offset);
        if (b) {
            throw new NumberNotAvailableException(l);
        }
        bits.set(offset, true);
        return null;
    }

    public Block takeHead(NumPattern pattern) {
        return null;
    }

    public Block takeTail(NumPattern pattern) {
        return null;
    }

    private int size() {
        return (int) (max - min + 1);
    }

    private int offset(long l) {
        return (int) (l - min0);
    }
}
