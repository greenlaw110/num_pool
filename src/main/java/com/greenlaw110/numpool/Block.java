package com.greenlaw110.numpool;

/**
 * Represent a block of number
 */
public interface Block {

    /**
     * The first number in this block
     * @return the head of this block
     */
    long min();

    /**
     * The last number in the block
     * @return the tail of this block
     */
    long max();

    /**
     * Consume a number from this block and return a {@link BlockPair} instance
     * which contains the two blocks split by the number specified.
     *
     * Note it is possible a block NOT split by the number for certain type of
     * blocks, e.g. {@link BitSetBlock}. In which case calling this function will
     * return {@code null}
     *
     * @param l the number to be taken from the block
     * @return a pair of blocks splited by the number, or {@code null} if the
     *         block is NOT splitable
     * @throws IllegalStateException if the number is not available in the block
     */
    BlockPair consume(long l);

    /**
     * Supply a number into a block and return a {@link BlockTriple} instance
     * which contains three blocks after the number inserted.
     *
     * When a number is inserted into a block the following cases might happen:
     *
     * <ul>
     *     <li>Inject number into a {@link SeqBlock} will trigger the {@link IllegalStateException}</li>
     *     <li>Inject number into a {@link BitSetBlock} will change the block state and return {@code null}</li>
     *     <li>Inject number into a {@link HopBlock} will split the block into three blocks</li>
     * </ul>
     *
     * @param l the number to be inserted into the block
     * @return a {@link BlockTriple} instance or {@code null} if block is not splitable
     */
    BlockTriple supply(long l);

    /**
     * Take out a sub block that matches the number pattern specified from head of this
     * block.
     *
     * @param pattern the number pattern of the sub block
     * @return the matched block or {@code null} if no such sub block exists
     */
    Block takeHead(NumPattern pattern);

    /**
     * Take out a sub block that matches the number pattern specified from the tail of this
     * block
     * @param pattern
     * @return
     */
    Block takeTail(NumPattern pattern);

    /**
     * Return string representation of this block
     * @return the string representation of this block
     */
    String toString();

    /**
     * Check if the block is empty, ie. all numbers inside has been consumed
     * @return {@code true} if the block is empty
     */
    boolean isEmpty();

}
