package com.greenlaw110.numpool;

/**
 * Represent a arithmetic number series with 1 hop between
 * each number
 */
class HopBlock implements Block {
    private long min;
    private long max;

    HopBlock(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public long min() {
        return min;
    }

    public long max() {
        return max;
    }

    public boolean isEmpty() {
        return false;
    }

    public BlockPair consume(long l) {
        return null;
    }

    public BlockTriple supply(long l) {
        return null;
    }

    public Block takeHead(NumPattern pattern) {
        return null;
    }

    public Block takeTail(NumPattern pattern) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        long l = min;
        while (l < max) {
            sb.append(l).append(",");
        }
        sb.append(max);
        return sb.toString();
    }

}
