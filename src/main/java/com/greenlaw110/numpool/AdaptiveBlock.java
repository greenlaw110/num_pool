package com.greenlaw110.numpool;

import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;

import static com.greenlaw110.numpool.Position.LEFT;
import static com.greenlaw110.numpool.Position.ON;
import static com.greenlaw110.numpool.Position.RIGHT;

/**
 * The default implementation of {@link Block}.
 *
 * <p>
 *     {@code AdaptiveBlock} is able to store block of consecutive numbers or adapt into
 *     a {@link java.util.BitSet} representing non-consecutive numbers
 * </p>
 */
class AdaptiveBlock implements Block {

    private long min;
    private long max;
    private BitSet bitSet;

    AdaptiveBlock(long n) {
        this(n, n);
    }

    AdaptiveBlock(long min, long max) {
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
     * The logic of this function:
     * <ul>
     *     <li>When l is out of Range then throw out an exception</li>
     *     <li>When l equals to min or max, update block scope and return {@code null}</li>
     *     <li>
     *         otherwise return the split block pair or update the bitSet status and return
     *         {@code null} depending on the {@link NumPoolConfig#bitSetBlockWords} setting
     *     </li>
     * </ul>
     * @param n the number to be taken out from the block
     * @return the block pair if this block splits
     */
    public BlockPair take(long n) {
        if (n < min || n > max) {
            throw new NumberOutOfRangeException(n);
        }
        if (n == min) {
            shrinkLeft();
        } else if (n == max) {
            shrinkRight();
        } else {
            if (null != bitSet) {
                int offset = offset(n);
                if (!bitSet.get(offset)) {
                    throw new NumberNotAvailableException(n);
                }
                bitSet.set(offset, false);
            } else {
                if (isOverflow(size())) {
                    // need to split
                    Block prefix = new AdaptiveBlock(min, n - 1);
                    Block suffix = new AdaptiveBlock(n + 1, max);
                    return new BlockPair(prefix, suffix);
                } else {
                    // convert consecutive block to bitset block
                    bitSet = new BitSet(bits());
                    int limit = (int) (max + 1 - min);
                    for (int i = 0; i < limit; ++i) {
                        bitSet.set(i, true);
                    }
                    bitSet.set(offset(n), false);
                }
            }
        }
        return null;
    }

    /**
     * If supplied number is one less than {@link #min} then
     * take it and set the min to that number
     *
     * if supplied number is one larger than {@link #max} then
     * take it and set the max to that number
     *
     * For all other cases it's an error
     */
    public BlockPair offer(long n) {
        if (n + 1 == min) {
            return expandLeft();
        } else if (n - 1 == max) {
            return expandRight();
        }
        if (n < min) {
            throw new NumberNotAvailableException(n);
        }
        if (n > max) {
            if (!isOverflow(n - min + 1)) {
                max = n;
                bitSet.set(offset(max), true);
                return null;
            }
            boolean x = isOverflow(n - min + 1);
            Position p = positionTo(n);
            throw new NumberNotAvailableException(n);
        }
        if (null != bitSet) {
            int offset = offset(n);
            if (bitSet.get(offset)) {
                throw new NumberNotAvailableException(n);
            }
            bitSet.set(offset, true);
            if (bitSet.nextClearBit(0) > offset(max)) {
                // bitset block now revert back to consecutive block
                bitSet = null;
            }
        }
        return null;
    }

    public Position positionTo(Block block) {
        if (this.min > block.max()) {
            return RIGHT;
        }
        if (this.max < block.min()) {
            return LEFT;
        }
        return ON;
    }

    @Override
    public Position positionTo(long n) {
        if (this.min > n) {
            return RIGHT;
        }

        if (null == bitSet) {
            return this.max < n ? LEFT : ON;
        } else {
            return this.min + bits() <= n ? LEFT : ON;
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
        if (null == bitSet) {
            sb.append(min).append("-").append(max);
        } else {
            boolean appended = false;
            int i = 0;
            int j = bitSet.nextClearBit(i);
            while (i > -1 && j <= max) {
                if (appended) {
                    sb.append(",");
                }
                sb.append(dereference(i));
                appended = true;
                if (j > i + 1) {
                    sb.append("-");
                    sb.append(dereference(j - 1));
                }
                i = bitSet.nextSetBit(j);
                if (i > -1) {
                    j = bitSet.nextClearBit(i);
                    if (j > max) {
                        if (appended) {
                            sb.append(",");
                        }
                        sb.append(dereference(i));
                        if (j > i + 1) {
                            sb.append("-");
                            sb.append(max);
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    public void write(Writer writer) throws IOException {
        if (isEmpty()) {
            writer.write("E");
        }
        if (max == min) {
            writer.write(String.valueOf(max));
        }
        if (null == bitSet) {
            writer.write(String.valueOf(min));
            writer.write("-");
            writer.write(String.valueOf(max));
        } else {
            boolean appended = false;
            int i = 0;
            int j = bitSet.nextClearBit(i);
            while (i > -1 && j <= max) {
                if (appended) {
                    writer.write(",");
                }
                writer.write(String.valueOf(dereference(i)));
                appended = true;
                if (j > i + 1) {
                    writer.write("-");
                    writer.write(String.valueOf(dereference(j - 1)));
                }
                i = bitSet.nextSetBit(j);
                if (i > -1) {
                    j = bitSet.nextClearBit(i);
                    if (j > max) {
                        if (appended) {
                            writer.write(",");
                        }
                        writer.write(String.valueOf(dereference(i)));
                        if (j > i + 1) {
                            writer.write("-");
                            writer.write(String.valueOf(max));
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mergeInto(Block block) {
        Position position = positionTo(block);
        if (ON == position) {
            throw new IllegalArgumentException("block cannot merge itself");
        }
        AdaptiveBlock that = (AdaptiveBlock) block;
        AdaptiveBlock little = this;
        AdaptiveBlock large = that;
        if (RIGHT == position) {
            little = that;
            large = this;
        }
        if (bitSet == null && that.bitSet == null) {
            // check affiliation
            if (little.max + 1 == large.min) {
                if (little == this) {
                    that.min = this.min;
                } else {
                    that.max = this.max;
                }
                return true;
            }
        }
        // check scope
        long gap = large.max - little.min() + 1;
        if (!isOverflow(gap)) {
            BitSet bitSet = new BitSet(bits());
            int limit = (int) (little.max + 1 - little.min);
            for (int i = 0; i < limit; ++i) {
                boolean set = null == little.bitSet ? true : little.bitSet.get(i);
                bitSet.set(i, set);
            }

            int offset = (int)(large.min - little.min);
            limit = (int) (large.max + 1 - large.min);
            for (int i = 0; i < limit; ++i) {
                boolean set = null == large.bitSet ? true : large.bitSet.get(i);
                bitSet.set(i + offset, set);
            }
            that.bitSet = bitSet;
            if (little == this) {
                that.min = this.min;
            } else {
                that.max = this.max;
            }
            return true;
        }
        return false;
    }

    @Override
    public long size() {
        return max + 1 - min;
    }

    private void shrinkLeft() {
        min++;
        if (isEmpty()) {
            bitSet = null;
            return;
        }
        if (null != bitSet) {
            int offset = bitSet.nextSetBit(1);
            bitSet = bitSet.get(offset, bitSet.length());
//            long[] la = bitSet.toLongArray();
//            int len = la.length;
//            for (int i = 0; i < len; ++i) {
//                la[i] = la[i] >>> offset;
//            }
//            bitSet = BitSet.valueOf(la);
            min += offset - 1;
        }
    }

    private void shrinkRight() {
        max--;
        if (isEmpty()) {
            bitSet = null;
            return;
        }
        if (null != bitSet) {
            int offset = offset(max + 1);
            bitSet.set(offset, false);
            max = dereference(bitSet.previousSetBit(offset));
        }
    }

    private BlockPair expandLeft() {
        if (isOverflow(size() + 1)) {
            return new BlockPair(new AdaptiveBlock(min - 1), this);
        }
        if (null != bitSet) {
            long[] la = bitSet.toLongArray();
            int len = la.length;
            for (int i = 0; i < len; ++i) {
                la[i] = la[i] << 1;
            }
            bitSet = BitSet.valueOf(la);
            bitSet.set(0, true);
        }
        min--;
        return null;
    }

    private BlockPair expandRight() {
        if (isOverflow(size() + 1)) {
            return new BlockPair(this, new AdaptiveBlock(max + 1));
        }
        max++;
        if (null != bitSet) {
            bitSet.set(offset(max), true);
        }
        return null;
    }

    private int offset(long n) {
        return (int) (n - min);
    }

    private long dereference(int offset) {
        return min + offset;
    }

    private static int bits() {
        int words = NumPoolConfig.bitSetBlockWords();
        return words * 64;
    }

    public static boolean isOverflow(long size) {
        return size > bits();
    }
}
