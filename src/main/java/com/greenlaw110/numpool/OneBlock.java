package com.greenlaw110.numpool;

/**
 * A block with one number only
 */
class OneBlock implements Block {

    private long l;
    boolean consumed;

    OneBlock(long l) {
        this.l = l;
    }

    public long min() {
        return l;
    }

    public long max() {
        return l;
    }

    public BlockPair consume(long l) {
        if (l != this.l) {
            throw new NumberOutOfRangeException(l);
        } else if (consumed) {
            throw new NumberNotAvailableException(l);
        }
        consumed = true;
        return null;
    }

    public BlockTriple supply(long l) {
        if (l != this.l) {
            throw new NumberOutOfRangeException(l);
        } else if (!consumed) {
            throw new NumberNotAvailableException(l);
        }
        consumed = false;
        return null;
    }

    public Block takeHead(NumPattern pattern) {
        return this;
    }

    public Block takeTail(NumPattern pattern) {
        return this;
    }

    public boolean isEmpty() {
        return consumed;
    }

    @Override
    public String toString() {
        if (consumed) {
            return "";
        }
        return String.valueOf(l);
    }
}
