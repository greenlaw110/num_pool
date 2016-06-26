package com.greenlaw110.numpool;

/**
 * Configuration that impact the behavior of this library
 */
public final class NumPoolConfig {

    public static final int DEFAULT_BITSET_BLOCK_WORDS = 1;

    /**
     * The number of words (long) a single {@link AdaptiveBlock}
     * could persist. This configuration indicate the maximum of
     * consecutive number range that should be put into a {@link java.util.BitSet}
     * backed {@link Block}.
     *
     * <p>
     *     For example, if the words limit is 2, which means the bit set could
     *     represent a range of 16 (2 * 8) number, for any consecutive number
     *     sequence range is less than 16, it can be represented with the
     *     bit set backed block; for a consecutive number sequence range over
     *     16, it shall use the sequence block to represent
     * </p>
     */
    private int bitSetBlockWords = DEFAULT_BITSET_BLOCK_WORDS;

    public NumPoolConfig(int bitSetBlockWords) {
        this.bitSetBlockWords = bitSetBlockWords;
    }

    /**
     * Get {@link #bitSetBlockWords} configuration from the
     * {@link ThreadLocal} variable
     * @return the number of words(long) that can be used in one {@code BitSet}
     *         backed block
     */
    static int bitSetBlockWords() {
        NumPoolConfig config = threadLocal.get();
        return null == config ? DEFAULT_BITSET_BLOCK_WORDS : config.bitSetBlockWords;
    }

    /**
     * Create a configuration instance with bitset words specified and set it to
     * thread local storage
     * @param words the number of words a bitset can have at most
     */
    static void configureBitSetWords(int words) {
        threadLocal.set(new NumPoolConfig(words));
    }

    private static final ThreadLocal<NumPoolConfig> threadLocal = new ThreadLocal<>();
}
