package com.greenlaw110.numpool;

import org.junit.Assert;

/**
 * Provides utility meethods for unit test cases
 */
public class TestBase extends Assert {
    protected static void isNull(Object a) {
        assertNull(a);
    }

    protected static void notNull(Object a) {
        assertNotNull(a);
    }

    protected static void same(Object a, Object b) {
        assertSame(a, b);
    }

    protected static void eq(Object[] a1, Object[] a2) {
        assertArrayEquals(a1, a2);
    }

    protected static void eq(Object o1, Object o2) {
        assertEquals(o1, o2);
    }

    protected static void ne(Object o1, Object o2) {
        assertNotEquals(o1, o2);
    }

    protected static void yes(Boolean expr, String msg, Object... args) {
        assertTrue(String.format(msg, args), expr);
    }


    protected static void yes(Boolean expr) {
        assertTrue(expr);
    }

    protected static void no(Boolean expr, String msg, Object... args) {
        assertFalse(String.format(msg, args), expr);
    }

    protected static void no(Boolean expr) {
        assertFalse(expr);
    }

    protected static void fail(String msg, Object... args) {
        assertFalse(String.format(msg, args), true);
    }

    protected static void echo(String tmpl, Object... args) {
        System.out.println(String.format(tmpl, args));
    }

}
