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

import static java9.util.stream.LambdaTestHelpers.countTo;

import java.util.ArrayList;

import java9.util.DoubleSummaryStatistics;
import java9.util.IntSummaryStatistics;

import java.util.List;

import java9.util.LongSummaryStatistics;
import java9.util.stream.Collectors;

import org.testng.annotations.Test;

import java9.util.stream.StreamSupport;
import java9.util.stream.OpTestCase;

/**
 * TestSummaryStatistics
 *
 * @author Brian Goetz
 */
@Test
public class SummaryStatisticsTest extends OpTestCase {
    public void testIntStatistics() {
        List<IntSummaryStatistics> instances = new ArrayList<>();
        instances.add(StreamSupport.stream(countTo(1000)).collect(Collectors.summarizingInt(i -> i)));
        instances.add(StreamSupport.stream(countTo(1000)).mapToInt(i -> i).summaryStatistics());
        instances.add(StreamSupport.stream(countTo(1000), 0, true).collect(Collectors.summarizingInt(i -> i)));
        instances.add(StreamSupport.stream(countTo(1000), 0, true).mapToInt(i -> i).summaryStatistics());

        for (IntSummaryStatistics stats : instances) {
            assertEquals(stats.getCount(), 1000);
            assertEquals(stats.getSum(), StreamSupport.stream(countTo(1000)).mapToInt(i -> i).sum());
            assertEquals(stats.getMax(), 1000);
            assertEquals(stats.getMin(), 1);
        }
    }

    public void testLongStatistics() {
        List<LongSummaryStatistics> instances = new ArrayList<>();
        instances.add(StreamSupport.stream(countTo(1000)).collect(Collectors.summarizingLong(i -> i)));
        instances.add(StreamSupport.stream(countTo(1000)).mapToLong(i -> i).summaryStatistics());
        instances.add(StreamSupport.stream(countTo(1000), 0, true).collect(Collectors.summarizingLong(i -> i)));
        instances.add(StreamSupport.stream(countTo(1000), 0, true).mapToLong(i -> i).summaryStatistics());

        for (LongSummaryStatistics stats : instances) {
            assertEquals(stats.getCount(), 1000);
            assertEquals(stats.getSum(), (long) StreamSupport.stream(countTo(1000)).mapToInt(i -> i).sum());
            assertEquals(stats.getMax(), 1000L);
            assertEquals(stats.getMin(), 1L);
        }
    }

    public void testDoubleStatistics() {
        List<DoubleSummaryStatistics> instances = new ArrayList<>();
        instances.add(StreamSupport.stream(countTo(1000)).collect(Collectors.summarizingDouble(i -> i)));
        instances.add(StreamSupport.stream(countTo(1000)).mapToDouble(i -> i).summaryStatistics());
        instances.add(StreamSupport.stream(countTo(1000), 0, true).collect(Collectors.summarizingDouble(i -> i)));
        instances.add(StreamSupport.stream(countTo(1000), 0, true).mapToDouble(i -> i).summaryStatistics());

        for (DoubleSummaryStatistics stats : instances) {
            assertEquals(stats.getCount(), 1000);
            assertEquals(stats.getSum(), (double) StreamSupport.stream(countTo(1000)).mapToInt(i -> i).sum());
            assertEquals(stats.getMax(), 1000.0);
            assertEquals(stats.getMin(), 1.0);
        }
    }
}
