/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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
package org.openjdk.tests.java.util.stream;

import java9.util.stream.IntStream;
import java9.util.stream.IntStreams;
import java9.util.stream.IntStreamTestDataProvider;
import java9.util.stream.OpTestCase;
import java9.util.stream.TestData;

import static java9.util.stream.LambdaTestHelpers.*;

import org.testng.annotations.Test;

import java9.util.J8Arrays;

import java9.util.OptionalInt;

public class IntReduceTest extends OpTestCase {
    public void testReduce() {
        int[] a = IntStreams.range(1, 11).toArray();

        assertEquals(55, J8Arrays.stream(a).reduce(irPlus).getAsInt());
        assertEquals(55, J8Arrays.stream(a).reduce(0, irPlus));
        assertEquals(10, J8Arrays.stream(a).reduce(irMax).getAsInt());
        assertEquals(1, J8Arrays.stream(a).reduce(irMin).getAsInt());

        assertEquals(0, IntStreams.empty().reduce(0, irPlus));
        assertFalse(IntStreams.empty().reduce(irPlus).isPresent());

        assertEquals(110, J8Arrays.stream(a).map(irDoubler).reduce(irPlus).getAsInt());
        assertEquals(20, J8Arrays.stream(a).map(irDoubler).reduce(irMax).getAsInt());
        assertEquals(2, J8Arrays.stream(a).map(irDoubler).reduce(irMin).getAsInt());
    }

    @Test(dataProvider = "IntStreamTestData", dataProviderClass = IntStreamTestDataProvider.class)
    public void testOps(String name, TestData.OfInt data) {
        assertEquals(0, (int) exerciseTerminalOps(data, (IntStream s) -> s.filter(ipFalse), s -> s.reduce(0, irPlus)));

        OptionalInt seedless = exerciseTerminalOps(data, s -> s.reduce(irPlus));
        int folded = exerciseTerminalOps(data, s -> s.reduce(0, irPlus));
        assertEquals(folded, seedless.orElse(0));

        seedless = exerciseTerminalOps(data, s -> s.reduce(irMin));
        folded = exerciseTerminalOps(data, s -> s.reduce(Integer.MAX_VALUE, irMin));
        assertEquals(folded, seedless.orElse(Integer.MAX_VALUE));

        seedless = exerciseTerminalOps(data, s -> s.reduce(irMax));
        folded = exerciseTerminalOps(data, s -> s.reduce(Integer.MIN_VALUE, irMax));
        assertEquals(folded, seedless.orElse(Integer.MIN_VALUE));

        seedless = exerciseTerminalOps(data, (IntStream s) -> s.map(irDoubler), s -> s.reduce(irPlus));
        folded = exerciseTerminalOps(data, (IntStream s) -> s.map(irDoubler), s -> s.reduce(0, irPlus));
        assertEquals(folded, seedless.orElse(0));

        seedless = exerciseTerminalOps(data, (IntStream s) -> s.map(irDoubler), s -> s.reduce(irMin));
        folded = exerciseTerminalOps(data, (IntStream s) -> s.map(irDoubler), s -> s.reduce(Integer.MAX_VALUE, irMin));
        assertEquals(folded, seedless.orElse(Integer.MAX_VALUE));

        seedless = exerciseTerminalOps(data, (IntStream s) -> s.map(irDoubler), s -> s.reduce(irMax));
        folded = exerciseTerminalOps(data, (IntStream s) -> s.map(irDoubler), s -> s.reduce(Integer.MIN_VALUE, irMax));
        assertEquals(folded, seedless.orElse(Integer.MIN_VALUE));
    }
}
