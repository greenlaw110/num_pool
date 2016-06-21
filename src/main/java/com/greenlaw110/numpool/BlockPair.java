package com.greenlaw110.numpool;

/**
 * A datastructure to hold two blocks
 */
public class BlockPair {
    private Block a;
    private Block b;

    public BlockPair(Block a, Block b) {
        this.a = a;
        this.b = b;
    }

    public Block a() {
        return a;
    }

    public Block b() {
        return b;
    }
}
