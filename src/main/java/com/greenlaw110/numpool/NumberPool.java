package com.greenlaw110.numpool;

import java.util.Objects;
import java.util.TreeSet;

public class NumberPool {

    private static class Node implements Comparable<Node> {

        private Block block;
        private Node prefix;
        private Node suffix;

        Node(Block block) {
            this.block = block;
        }

        private boolean hasSuffix() {
            return null != suffix;
        }

        private boolean hasPrefix() {
            return null != prefix;
        }

        @Override
        public String toString() {
            return block.toString();
        }

        public int compareTo(Node o) {
            return BlockComparator.INSTANCE.compare(block, o.block);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(block.min());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Node) {
                Node that = (Node) obj;
                return that.block.min() == block.min();
            }
            return false;
        }
    }

    enum Position {
        LEFT, RIGHT, ON
    }

    private Node head;
    private TreeSet<Node> index = new TreeSet<Node>();

    public NumberPool(long max) {
        head = new Node(new SeqBlock(0, max - 1));
        index.add(head);
    }

    public synchronized void consume(long l) {
        if (l < 0) {
            throw new IllegalArgumentException("number cannot be negative");
        }
        Node node = findBlock(l, false);
        if (null == node) {
            throw new NumberNotAvailableException(l);
        }
        BlockPair pair = node.block.consume(l);
        if (null != pair) {
            Node na = new Node(pair.a());
            if (null != node.prefix) {
                node.prefix.suffix = na;
                na.prefix = node.prefix;
                index.remove(node);
            } else {
                index.remove(head);
                head = na;
            }
            Node nb = new Node(pair.b());
            nb.prefix = na;
            na.suffix = nb;
            nb.suffix = node.suffix;
            if (null != node.suffix) {
                node.suffix.prefix = nb;
            }
            index.add(na);
            index.add(nb);
        } else {
            if (node.block.isEmpty()) {
                if (node.prefix != null) {
                    node.prefix.suffix = node.suffix;
                    node.suffix.prefix = node.prefix;
                } else {
                    head.suffix = node.suffix;
                    node.suffix.prefix = head;
                }
                index.remove(node);
            }
        }
    }

    public synchronized void supply(long l) {
        if (l < 0) {
            throw new IllegalArgumentException("number cannot be negative");
        }
        Node node = findBlock(l, true);
        if (null == node) {
            throw new IllegalStateException("Unknown error");
        }
        Position p = position(l, node.block);
        if (Position.ON == p) {
            BlockTriple triple = node.block.supply(l);
            if (null != triple) {
                index.remove(node);
                Node a = new Node(triple.a());
                Node b = new Node(triple.b());
                Node c = new Node(triple.c());
                a.suffix = b;
                b.suffix = c;
                c.suffix = node.suffix;
                node.suffix.prefix = c;
                c.prefix = b;
                b.prefix = a;
                a.prefix = node.prefix;
                node.prefix.suffix = a;
                index.add(a);
                index.add(b);
                index.add(c);
            }
        } else {
            Block block = new OneBlock(l);
            Node newNode = new Node(block);
            newNode.prefix = node;
            newNode.suffix = node.suffix;
            if (null != node.suffix) {
                node.suffix.prefix = newNode;
            }
            node.suffix = newNode;
            index.add(newNode);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node node = head;
        while (null != node) {
            sb.append(node.block).append("\n");
            node = node.suffix;
        }
        return sb.toString();
    }

    private Node findBlock(long l, boolean includeNeighbour) {
        Node node = index.floor(new Node(new OneBlock(l)));
        if (null == node) {
            return null;
        }
        if (!includeNeighbour && Position.ON != position(l, node.block)) {
            return null;
        }
        return node;
//        Node node = head;
//        while (true) {
//            Block b = node.block;
//            Position position = position(l, b);
//            switch (position) {
//                case LEFT:
//                    node = node.suffix;
//                    if (null == node) {
//                        return null;
//                    }
//                    continue;
//                case RIGHT:
//                    return includeNeighbour ? node : null;
//                default:
//                    return node;
//            }
//        }
    }

    private static Position position(long l, Block block) {
        if (block.max() < l) {
            return Position.LEFT;
        } else if (block.min() > l) {
            return Position.RIGHT;
        } else {
            return Position.ON;
        }
    }
}
