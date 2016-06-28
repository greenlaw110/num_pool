package com.greenlaw110.numpool;

/**
 * Represent a block of number. The number block contains a sequence of
 * ascending ordered numbers starting from {@link #min()} to {@link #max()}.
 */
public interface Block {

    /**
     * The first (most left hand side) number in this block
     * @return the head of this block
     */
    long min();

    /**
     * The last (most right hand side) number in the block
     * @return the tail of this block
     */
    long max();

    /**
     * Take a number out from this block and return a {@link BlockPair} instance
     * which contains the two blocks split by the number specified.
     *
     * <p>
     *     <b>Note</b> certain type of Block does not split when number has
     *     been taken out of it, e.g. the {@link BitSetBlock}, in which case
     *     the block will maintain a state remember the number has been taken out
     *     and calling to this function will return {@code null}
     * </p>
     *
     * @param n the number to be taken from the block
     * @return a pair of blocks splited by the number, or {@code null} if the
     *         block does not split
     * @throws NumberNotAvailableException if the number is not available in the block
     */
    BlockPair checkOut(long n);

    /**
     * Offer a number into this block
     *
     * <ul>
     *     <li>
     *         If the number is affiliated to the block, i.e. value is one less than
     *         {@link #min()} or one more than {@link #max()}, it will trigger the
     *         block expand backward or forward by one if there are room for block
     *         size to increase; Or if the block size is restricted and the capacity
     *         of the block is full, then it will return an instance of {@link BlockPair}
     *         with an new block (host the new number) and itself
     *     </li>
     *     <li>
     *         If the number is outside the block completely, then {@link NumberNotAvailableException}
     *         will be thrown out
     *     </li>
     *     <li>
     *         If the number is inside the block, If number is inserted, then
     *         the block internal state will be updatd and {@link null} will be thrown out.
     *         In case the same number already exists then {@link NumberNotAvailableException}
     *         will be thrown out
     *     </li>
     * </ul>
     *
     * @param l the number to be inserted into the block
     * @return
     * @throws NumberNotAvailableException if the number cannot be offered into the block
     */
    BlockPair checkIn(long l);

    /**
     * Return string representation of this block
     * @return the string representation of this block
     */
    String toString();

    /**
     * Return the size of the block. The number should be equal to
     * {@code max + 1 - min}
     * @return the block size
     */
    long size();

    /**
     * Check if the block is empty, ie. all numbers inside has been consumed
     * @return {@code true} if the block is empty
     */
    boolean isEmpty();

    /**
     * Returns the position of this block in relation to the
     * specified block.
     *
     * <p>This function returns {@link Position}
     * enum value in accordance to the following logic:</p>
     *
     * <ul>
     *     <li>
     *         {@link Position#LEFT} - when
     *         this block's {@code max} number is less than the given block's
     *         {@code min} number
     *     </li>
     *     <li>
     *         {@link Position#RIGHT} - when
     *         this block's {@code min} number is greater than the given block's
     *         {@code max} number
     *     </li>
     *     <li>
     *         {@link Position#ON} - other cases
     *     </li>
     * </ul>
     *
     *
     * @param block the block to which this block is compare to
     * @return the position of this block in relation to the target block
     */
    Position positionTo(Block block);

    /**
     * Returns the position of this block to a number
     * @param n the number
     * @return the position
     */
    Position positionTo(long n);

    /**
     * Try to merge this block into given block.
     * @param block the target block this block to be merged into
     * @return {@code true} if merge successfully, or {@code false} otherwise
     */
    boolean mergeInto(Block block);

}
