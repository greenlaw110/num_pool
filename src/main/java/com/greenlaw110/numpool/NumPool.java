package com.greenlaw110.numpool;

import java.io.IOException;
import java.io.Writer;
import java.util.TreeSet;

/**
 * The second version of NumPool implementation
 */
public class NumPool {

    private static class NumHolder implements Block {

        private static final ThreadLocal<NumHolder> threadLocal = new ThreadLocal<NumHolder>(){
            @Override
            protected NumHolder initialValue() {
                return new NumHolder(0);
            }
        };

        private long n;
        NumHolder(long n) {
            this.n = n;
        }

        @Override
        public long min() {
            return n;
        }

        @Override
        public long max() {
            return n;
        }

        @Override
        public BlockPair take(long n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BlockPair offer(long l) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Position positionTo(Block block) {
            return block.positionTo(this).revert();
        }

        @Override
        public Position positionTo(long n) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean mergeInto(Block block) {
            throw new UnsupportedOperationException();
        }

        static void set(long n) {
            threadLocal.get().n = n;
        }

        static NumHolder get() {
            return threadLocal.get();
        }

    }

    private final TreeSet<Block> blocks = new TreeSet<>(BlockComparator.INSTANCE);
    private long min;
    private long max;

    public NumPool(long min, long max) {
        blocks.add(new AdaptiveBlock(min, max));
        this.min = min;
        this.max = max;
    }

    public synchronized void take(long n) {
        checkRange(n);
        Block block = locate(n);
        if (block.positionTo(n).isOn()) {
            // it has to remove->change->add back
            // in order to refresh the treeset's internal state
            // while element state changed
            beforeUpdate(block);
            BlockPair pair = block.take(n);
            afterUpdate(block);
            if (null != pair) {
                blocks.remove(block);
                blocks.add(pair.a());
                blocks.add(pair.b());
            } else if (block.isEmpty()) {
                blocks.remove(block);
            }
        } else {
            throw new NumberNotAvailableException(n);
        }
    }

    public synchronized void offer(long n) {
        checkRange(n);
        Block block = locate(n);
        if (null == block) {
            AdaptiveBlock newBlock = new AdaptiveBlock(n);
            block = locateRight(n);
            boolean ok = false;
            if (null != block) {
                beforeUpdate(block);
                ok = newBlock.mergeInto(block);
                afterUpdate(block);
            }
            if (!ok) {
                blocks.add(newBlock);
            }
        } else {
            if (block.positionTo(n).isOn()) {
                beforeUpdate(block);
                BlockPair pair = block.offer(n);
                afterUpdate(block);
                if (null != pair) {
                    blocks.remove(block);
                    blocks.add(pair.a());
                    blocks.add(pair.b());
                }
            } else {
                AdaptiveBlock newBlock = new AdaptiveBlock(n);
                beforeUpdate(block);
                boolean ok = newBlock.mergeInto(block);
                afterUpdate(block);
                if (!ok) {
                    block = locateRight(n);
                    if (null != block) {
                        beforeUpdate(block);
                        ok = newBlock.mergeInto(block);
                        afterUpdate(block);
                    }
                    if (!ok) {
                        blocks.add(newBlock);
                    }
                }
            }
        }
    }

    public int blockCount() {
        return blocks.size();
    }

    @Override
    public String toString() {
        return toString(",");
    }

    public String toString(String separator) {
        boolean populated = false;
        StringBuilder sb = new StringBuilder();
        for (Block block : blocks) {
            if (populated) {
                sb.append(separator);
            } else {
                populated = true;
            }
            sb.append(block);
        }
        return sb.toString();
    }

    public void write(Writer writer, String separator) throws IOException {
        boolean populated = false;
        for (Block block : blocks) {
            if (populated) {
                writer.write(separator);
            } else {
                populated = true;
            }
            ((AdaptiveBlock)block).write(writer);
        }
    }

    private Block locate(long n) {
        NumHolder.set(n);
        return blocks.floor(NumHolder.get());
    }

    private Block locateRight(long n) {
        NumHolder.set(n);
        return blocks.ceiling(NumHolder.get());
    }

    private void beforeUpdate(Block block) {
        blocks.remove(block);
    }

    private void afterUpdate(Block block) {
        blocks.add(block);
    }

    private void checkRange(long n) {
        if (min > n || max < n) {
            throw new NumberNotAvailableException(n);
        }
    }

}
