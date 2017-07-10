/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
package java9.util.stream;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java9.util.J8Arrays;
import java9.util.Spliterator;
import java9.util.stream.IntStream;
import java9.util.stream.LongStream;
import java9.util.stream.StreamSpliterators;
import java9.util.stream.StreamSupport;

import static java9.util.stream.Collectors.toList;

/**
 * @bug 8012987
 */
@Test
public class SliceSpliteratorTest extends LoggingTestCase {

    static class UnorderedContentAsserter<T> implements SpliteratorTestHelper.ContentAsserter<T> {
        Collection<T> source;

        UnorderedContentAsserter(Collection<T> source) {
            this.source = source;
        }

        @Override
        public void assertContents(Collection<T> actual, Collection<T> expected, boolean isOrdered) {
            if (isOrdered) {
                assertEquals(actual, expected);
            }
            else {
                assertEquals(actual.size(), expected.size());
                assertTrue(source.containsAll(actual));
            }
        }
    }

    interface SliceTester {
        void test(int size, int skip, int limit);
    }

    @DataProvider(name = "sliceSpliteratorDataProvider")
    public static Object[][] sliceSpliteratorDataProvider() {
        List<Object[]> data = new ArrayList<>();

        // SIZED/SUBSIZED slice spliterator

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Integer> source =  IntStream.range(0, size).boxed().collect(toList());

                SpliteratorTestHelper.testSpliterator(() -> {
                    Spliterator<Integer> s = J8Arrays.spliterator(StreamSupport.stream(source).toArray(Integer[]::new));

                    return new StreamSpliterators.SliceSpliterator.OfRef<>(s, skip, limit);
                });
            };
            data.add(new Object[]{"StreamSpliterators.SliceSpliterator.OfRef", r});
        }

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Integer> source =  IntStream.range(0, size).boxed().collect(toList());

                SpliteratorTestHelper.testIntSpliterator(() -> {
                    Spliterator.OfInt s = J8Arrays.spliterator(StreamSupport.stream(source).mapToInt(i->i).toArray());

                    return new StreamSpliterators.SliceSpliterator.OfInt(s, skip, limit);
                });
            };
            data.add(new Object[]{"StreamSpliterators.SliceSpliterator.OfInt", r});
        }

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Long> source =  LongStream.range(0, size).boxed().collect(toList());

                SpliteratorTestHelper.testLongSpliterator(() -> {
                    Spliterator.OfLong s = J8Arrays.spliterator(StreamSupport.stream(source).mapToLong(i->i).toArray());

                    return new StreamSpliterators.SliceSpliterator.OfLong(s, skip, limit);
                });
            };
            data.add(new Object[]{"StreamSpliterators.SliceSpliterator.OfLong", r});
        }

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Double> source =  LongStream.range(0, size).asDoubleStream().boxed().collect(toList());

                SpliteratorTestHelper.testDoubleSpliterator(() -> {
                    Spliterator.OfDouble s = J8Arrays.spliterator(StreamSupport.stream(source).mapToDouble(i->i).toArray());

                    return new StreamSpliterators.SliceSpliterator.OfDouble(s, skip, limit);
                });
            };
            data.add(new Object[]{"StreamSpliterators.SliceSpliterator.OfLong", r});
        }


        // Unordered slice spliterator

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Integer> source =  IntStream.range(0, size).boxed().collect(toList());
                final UnorderedContentAsserter<Integer> uca = new UnorderedContentAsserter<>(source);

                SpliteratorTestHelper.testSpliterator(() -> {
                    Spliterator<Integer> s = J8Arrays.spliterator(StreamSupport.stream(source).toArray(Integer[]::new));

                    return new StreamSpliterators.UnorderedSliceSpliterator.OfRef<>(s, skip, limit);
                }, uca);
            };
            data.add(new Object[]{"StreamSpliterators.UnorderedSliceSpliterator.OfRef", r});
        }

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Integer> source =  IntStream.range(0, size).boxed().collect(toList());
                final UnorderedContentAsserter<Integer> uca = new UnorderedContentAsserter<>(source);

                SpliteratorTestHelper.testIntSpliterator(() -> {
                    Spliterator.OfInt s = J8Arrays.spliterator(StreamSupport.stream(source).mapToInt(i->i).toArray());

                    return new StreamSpliterators.UnorderedSliceSpliterator.OfInt(s, skip, limit);
                }, uca);
            };
            data.add(new Object[]{"StreamSpliterators.UnorderedSliceSpliterator.OfInt", r});
        }

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Long> source =  LongStream.range(0, size).boxed().collect(toList());
                final UnorderedContentAsserter<Long> uca = new UnorderedContentAsserter<>(source);

                SpliteratorTestHelper.testLongSpliterator(() -> {
                    Spliterator.OfLong s = J8Arrays.spliterator(StreamSupport.stream(source).mapToLong(i->i).toArray());

                    return new StreamSpliterators.UnorderedSliceSpliterator.OfLong(s, skip, limit);
                }, uca);
            };
            data.add(new Object[]{"StreamSpliterators.UnorderedSliceSpliterator.OfLong", r});
        }

        {
            SliceTester r = (size, skip, limit) -> {
                final Collection<Double> source =  LongStream.range(0, size).asDoubleStream().boxed().collect(toList());
                final UnorderedContentAsserter<Double> uca = new UnorderedContentAsserter<>(source);

                SpliteratorTestHelper.testDoubleSpliterator(() -> {
                    Spliterator.OfDouble s = J8Arrays.spliterator(LongStream.range(0, SIZE).asDoubleStream().toArray());

                    return new StreamSpliterators.UnorderedSliceSpliterator.OfDouble(s, skip, limit);
                }, uca);
            };
            data.add(new Object[]{"StreamSpliterators.UnorderedSliceSpliterator.OfLong", r});
        }

        return data.toArray(new Object[0][]);
    }

    static final int SIZE = 256;

    static final int STEP = 32;

    @Test(dataProvider = "sliceSpliteratorDataProvider")
    public void testSliceSpliterator(String description, SliceTester r) {
        setContext("size", SIZE);
        for (int skip = 0; skip < SIZE; skip += STEP) {
            setContext("skip", skip);
            for (int limit = 0; limit < SIZE; limit += STEP) {
                setContext("limit", skip);
                r.test(SIZE, skip, limit);
            }
        }
    }
}
