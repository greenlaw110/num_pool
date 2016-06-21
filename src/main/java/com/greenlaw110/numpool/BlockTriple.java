package com.greenlaw110.numpool;

/**
 * A data structure stores three {@link Block blocks}
 */
public class BlockTriple extends BlockPair {
    private Block c;

    public BlockTriple(Block a, Block b, Block c) {
        super(a, b);
        this.c = c;
    }

    public Block c() {
        return c;
    }
}
