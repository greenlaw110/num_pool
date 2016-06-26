package com.greenlaw110.numpool;

/**
 * Defines the relationship between two {@link Block blocks} or a number to
 * a {@link Block block}
 */
public enum Position {
    /**
     * A number of a block's {@link Block#max()} is less than the target block's {@link Block#min()}
     */
    LEFT() {
        @Override
        public Position revert() {
            return RIGHT;
        }
    },

    /**
     * A number or a block's {@link Block#min()} is greater than the target blocks' {@link Block#max()}
     */
    RIGHT() {
        @Override
        public Position revert() {
            return LEFT;
        }
    },

    /**
     * A number is between {@link Block#min()} inclusive and {@link Block#max()} inclusive of target block;
     * <p>A block is itself</p>
     * <p>Note block overlay or embedding is not allowed in the system</p>
     */
    ON() {
        @Override
        public Position revert() {
            return this;
        }
    };

    /**
     * Returns the opposite position of this position
     * @return the position from an opposite view of this position
     */
    public abstract Position revert() ;

    public boolean isOn() {
        return this == ON;
    }

    public boolean isLeft() {
        return this == LEFT;
    }

    public boolean isRight() {
        return this == RIGHT;
    }
}
