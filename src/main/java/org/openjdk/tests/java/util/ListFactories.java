/*
 * Copyright (c) 2015, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.tests.java.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java9.util.Iterators;
import java9.util.Lists;
import java.util.ListIterator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

/*
 * @test
 * @bug 8048330
 * @summary Test convenience static factory methods on List.
 * @run testng ListFactories
 */

public class ListFactories {

    static final int NUM_STRINGS = 20; // should be larger than the largest fixed-arg overload
    static final String[] stringArray;
    static {
        String[] sa = new String[NUM_STRINGS];
        for (int i = 0; i < NUM_STRINGS; i++) {
            sa[i] = String.valueOf((char)('a' + i));
        }
        stringArray = sa;
    }

    // returns array of [actual, expected]
    static Object[] a(List<String> act, List<String> exp) {
        return new Object[] { act, exp };
    }

    @DataProvider(name="empty")
    public Iterator<Object[]> empty() {
        return Collections.singletonList(
            a(Lists.of(), asList())
        ).iterator();
    }

    @DataProvider(name="nonempty")
    public Iterator<Object[]> nonempty() {
        return asList(
            a(Lists.of("a"),
               asList("a")),
            a(Lists.of("a", "b"),
               asList("a", "b")),
            a(Lists.of("a", "b", "c"),
               asList("a", "b", "c")),
            a(Lists.of("a", "b", "c", "d"),
               asList("a", "b", "c", "d")),
            a(Lists.of("a", "b", "c", "d", "e"),
               asList("a", "b", "c", "d", "e")),
            a(Lists.of("a", "b", "c", "d", "e", "f"),
               asList("a", "b", "c", "d", "e", "f")),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g"),
               asList("a", "b", "c", "d", "e", "f", "g")),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g", "h"),
               asList("a", "b", "c", "d", "e", "f", "g", "h")),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g", "h", "i"),
               asList("a", "b", "c", "d", "e", "f", "g", "h", "i")),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"),
               asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")),
            a(Lists.of(stringArray),
               asList(stringArray))
        ).iterator();
    }

    @DataProvider(name="sublists")
    public Iterator<Object[]> sublists() {
        return asList(
            a(Lists.<String>of().subList(0,0),
               asList()),
            a(Lists.of("a").subList(0,0),
               asList("a").subList(0,0)),
            a(Lists.of("a", "b").subList(0,1),
               asList("a", "b").subList(0,1)),
            a(Lists.of("a", "b", "c").subList(1,3),
               asList("a", "b", "c").subList(1,3)),
            a(Lists.of("a", "b", "c", "d").subList(0,4),
               asList("a", "b", "c", "d").subList(0,4)),
            a(Lists.of("a", "b", "c", "d", "e").subList(0,3),
               asList("a", "b", "c", "d", "e").subList(0,3)),
            a(Lists.of("a", "b", "c", "d", "e", "f").subList(3, 5),
               asList("a", "b", "c", "d", "e", "f").subList(3, 5)),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g").subList(0, 7),
               asList("a", "b", "c", "d", "e", "f", "g").subList(0, 7)),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g", "h").subList(0, 0),
               asList("a", "b", "c", "d", "e", "f", "g", "h").subList(0, 0)),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g", "h", "i").subList(4, 5),
               asList("a", "b", "c", "d", "e", "f", "g", "h", "i").subList(4, 5)),
            a(Lists.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j").subList(1,10),
               asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j").subList(1,10)),
            a(Lists.of(stringArray).subList(5, NUM_STRINGS),
               asList(Arrays.copyOfRange(stringArray, 5, NUM_STRINGS)))
                ).iterator();
    }

    @DataProvider(name="all")
    public Iterator<Object[]> all() {
        List<Object[]> all = new ArrayList<>();
        Iterators.forEachRemaining(empty(), all::add);
        Iterators.forEachRemaining(nonempty(), all::add);
        Iterators.forEachRemaining(sublists(), all::add);
        return all.iterator();
    }

    @DataProvider(name="nonsublists")
    public Iterator<Object[]> nonsublists() {
        List<Object[]> all = new ArrayList<>();
        Iterators.forEachRemaining(empty(), all::add);
        Iterators.forEachRemaining(nonempty(), all::add);
        return all.iterator();
    }

    @Test(dataProvider="all", expectedExceptions=UnsupportedOperationException.class)
    public void cannotAddLast(List<String> act, List<String> exp) {
        act.add("x");
    }

    @Test(dataProvider="all", expectedExceptions=UnsupportedOperationException.class)
    public void cannotAddFirst(List<String> act, List<String> exp) {
        act.add(0, "x");
    }

    @Test(dataProvider="nonempty", expectedExceptions=UnsupportedOperationException.class)
    public void cannotRemove(List<String> act, List<String> exp) {
        act.remove(0);
    }

    @Test(dataProvider="nonempty", expectedExceptions=UnsupportedOperationException.class)
    public void cannotSet(List<String> act, List<String> exp) {
        act.set(0, "x");
    }

    @Test(dataProvider="all")
    public void contentsMatch(List<String> act, List<String> exp) {
        assertEquals(act, exp);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed1() {
        Lists.of((Object) null); // force one-arg overload
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed2a() {
        Lists.of("a", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed2b() {
        Lists.of(null, "b");
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed3() {
        Lists.of("a", "b", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed4() {
        Lists.of("a", "b", "c", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed5() {
        Lists.of("a", "b", "c", "d", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed6() {
        Lists.of("a", "b", "c", "d", "e", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed7() {
        Lists.of("a", "b", "c", "d", "e", "f", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed8() {
        Lists.of("a", "b", "c", "d", "e", "f", "g", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed9() {
        Lists.of("a", "b", "c", "d", "e", "f", "g", "h", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowed10() {
        Lists.of("a", "b", "c", "d", "e", "f", "g", "h", "i", null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullDisallowedN() {
        String[] array = stringArray.clone();
        array[0] = null;
        Lists.of(array);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void nullArrayDisallowed() {
        Lists.of((Object[])null);
    }

    @Test
    public void ensureArrayCannotModifyList() {
        String[] array = stringArray.clone();
        List<String> list = Lists.of(array);
        array[0] = "xyzzy";
        assertEquals(list, Arrays.asList(stringArray));
    }

    @Test(dataProvider="all", expectedExceptions=NullPointerException.class)
    public void containsNullShouldThrowNPE(List<String> act, List<String> exp) {
        act.contains(null);
    }

    @Test(dataProvider="all", expectedExceptions=NullPointerException.class)
    public void indexOfNullShouldThrowNPE(List<String> act, List<String> exp) {
        act.indexOf(null);
    }

    @Test(dataProvider="all", expectedExceptions=NullPointerException.class)
    public void lastIndexOfNullShouldThrowNPE(List<String> act, List<String> exp) {
        act.lastIndexOf(null);
    }

    // Lists.of().subList views should not be Serializable
    @Test(dataProvider="sublists")
    public void isNotSerializable(List<String> act, List<String> exp) {
        assertFalse(act instanceof Serializable);
    }

    // ... but Lists.of() should be
    @Test(dataProvider="nonsublists")
    public void serialEquality(List<String> act, List<String> exp) {
        // assume that act.equals(exp) tested elsewhere
        List<String> copy = serialClone(act);
        assertEquals(act, copy);
        assertEquals(copy, exp);
    }

    @SuppressWarnings("unchecked")
    static <T> T serialClone(T obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    List<Integer> genList() {
        return new ArrayList<>(Arrays.asList(1, 2, 3));
    }

    @Test
    public void copyOfResultsEqual() {
        List<Integer> orig = genList();
        List<Integer> copy = Lists.copyOf(orig);

        assertEquals(orig, copy);
        assertEquals(copy, orig);
    }

    @Test
    public void copyOfModifiedUnequal() {
        List<Integer> orig = genList();
        List<Integer> copy = Lists.copyOf(orig);
        orig.add(4);

        assertNotEquals(orig, copy);
        assertNotEquals(copy, orig);
    }

    @Test
    public void copyOfIdentity() {
        List<Integer> orig = genList();
        List<Integer> copy1 = Lists.copyOf(orig);
        List<Integer> copy2 = Lists.copyOf(copy1);

        assertNotSame(orig, copy1);
        assertSame(copy1, copy2);
    }

    @Test
    public void copyOfSubList() {
        List<Integer> orig = Lists.of(0, 1, 2, 3);
        List<Integer> sub = orig.subList(0, 3);
        List<Integer> copy = Lists.copyOf(sub);

        assertNotSame(sub, copy);
    }

    @Test
    public void copyOfSubSubList() {
        List<Integer> orig = Lists.of(0, 1, 2, 3);
        List<Integer> sub = orig.subList(0, 3).subList(0, 2);
        List<Integer> copy = Lists.copyOf(sub);

        assertNotSame(sub, copy);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void copyOfRejectsNullCollection() {
        @SuppressWarnings("unused")
        List<Integer> list = Lists.copyOf(null);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void copyOfRejectsNullElements() {
        @SuppressWarnings("unused")
        List<Integer> list = Lists.copyOf(Arrays.asList(1, null, 3));
    }

    @Test
    public void iteratorShouldNotBeListIterator() {
        List<Integer> list = Lists.of(1, 2, 3, 4, 5);
        Iterator<Integer> it = list.iterator();
        it.next();
        try {
            ((ListIterator<Integer>) it).previous();
            fail("ListIterator operation succeeded on Iterator");
        } catch (ClassCastException ignore) {
        } catch (UnsupportedOperationException ignore) {
        }
    }
}
