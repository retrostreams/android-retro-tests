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

import static java9.util.stream.LambdaTestHelpers.assertCountSum;
import static java9.util.stream.LambdaTestHelpers.assertUnique;

import java.util.Collection;

import org.testng.annotations.Test;

import java9.util.stream.IntStream;
import java9.util.stream.IntStreams;
import java9.util.stream.*;

/**
 * UniqOpTest
 */
@Test
public class IntUniqOpTest extends OpTestCase {

    public void testUniqOp() {
        assertCountSum(IntStreams.generate(() -> 0).limit(10).distinct().boxed(), 1, 0);
        assertCountSum(IntStreams.generate(() -> 1).limit(10).distinct().boxed(), 1, 1);
        assertCountSum(IntStreams.range(0, 0).distinct().boxed(), 0, 0);
        assertCountSum(IntStreams.range(1, 11).distinct().boxed(), 10, 55);
        assertCountSum(IntStreams.range(1, 11).distinct().boxed(), 10, 55);
    }

    @Test(dataProvider = "IntStreamTestData", dataProviderClass = IntStreamTestDataProvider.class)
    public void testOp(String name, TestData.OfInt data) {
        Collection<Integer> result = exerciseOps(data, (IntStream s) -> s.distinct().boxed());

        assertUnique(result);
        if (data.size() > 0)
            assertTrue(result.size() > 0);
        else
            assertTrue(result.size() == 0);
        assertTrue(result.size() <= data.size());
    }

    @Test(dataProvider = "IntStreamTestData", dataProviderClass = IntStreamTestDataProvider.class)
    public void testOpSorted(String name, TestData.OfInt data) {
        Collection<Integer> result = withData(data).
                stream((IntStream s) -> s.sorted().distinct().boxed()).
                exercise();

        assertUnique(result);
        if (data.size() > 0)
            assertTrue(result.size() > 0);
        else
            assertTrue(result.size() == 0);
        assertTrue(result.size() <= data.size());
    }
}
