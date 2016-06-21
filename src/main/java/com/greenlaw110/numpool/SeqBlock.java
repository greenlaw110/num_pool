package com.greenlaw110.numpool;

/**
 * Store {@link NumPattern#SEQUENCE} style numbers in a block
 */
class SeqBlock implements Block {
    private long min;
    private long max;

    SeqBlock(long min, long max) {
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
        return min > max;
    }

    /**
     * When l is out of Range then throw out an exception.
     * When l equals to min or max, then update block scope
     * otherwise return the split block pair
     * @param l the number to be taken from the block
     * @return the block pair if this block is split
     */
    public BlockPair consume(long l) {
        if (l < min || l > max) {
            throw new NumberOutOfRangeException(l);
        }
        if (l == min) {
            min++;
        } else if (l == max) {
            max--;
        } else {
            Block prefix = new SeqBlock(min, l - 1);
            Block suffix = new SeqBlock(l + 1, max);
            return new BlockPair(prefix, suffix);
        }
        return null;
    }

    /**
     * {@code SeqBlock} does not support supply method
     */
    public BlockTriple supply(long l) {
        throw new IllegalStateException("SeqBlock cannot insert a number");
    }

    public Block takeHead(NumPattern pattern) {
        switch (pattern) {
            case SEQUENCE:
                return this;
            default:
                return null;
        }
    }

    public Block takeTail(NumPattern pattern) {
        switch (pattern) {
            case SEQUENCE:
                return this;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "E";
        }
        if (max == min) {
            return String.valueOf(max);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(min).append("-").append(max);
        return sb.toString();
    }
}
