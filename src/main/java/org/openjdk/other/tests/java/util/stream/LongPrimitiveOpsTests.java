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
package org.openjdk.other.tests.java.util.stream;

import java9.util.function.LongPredicate;
import java9.util.function.LongSupplier;
import java9.util.function.LongUnaryOperator;
import java9.util.function.LongConsumer;
import java9.util.function.BinaryOperator;
import java9.util.function.ToLongFunction;
import java9.util.stream.Collectors;
import java9.util.stream.LongStream;
import java9.util.stream.Stream;
import java9.util.stream.StreamSupport;
import java9.util.J8Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.testng.Assert.assertEquals;

@Test
public class LongPrimitiveOpsTests {

    public void testSum() {
        long sum;
        sum = LongStream.range(1, 10).filter(new LongPredicate() {
            @Override
            public boolean test(long i) {
                return i % 2 == 0;
            }
        }).sum();
        assertEquals(sum, 20);
    }

    public void testMap() {
        long sum = LongStream.range(1, 10).filter(new LongPredicate() {
            @Override
            public boolean test(long i) {
                return i % 2 == 0;
            }
        }).map(new LongUnaryOperator() {
            @Override
            public long applyAsLong(long i) {
                return i * 2;
            }
        }).sum();
        assertEquals(sum, 40);
    }

    public void testParSum() {
        long sum = LongStream.range(1, 10).parallel().filter(new LongPredicate() {
            @Override
            public boolean test(long i) {
                return i % 2 == 0;
            }
        }).sum();
        assertEquals(sum, 20);
    }

    @Test(groups = { "serialization-hostile" })
    public void testTee() {
        final long[] teeSum = new long[1];
        long sum;
        sum = LongStream.range(1, 10).filter(new LongPredicate() {
            @Override
            public boolean test(long i) {
                return i % 2 == 0;
            }
        }).peek(new LongConsumer() {
            @Override
            public void accept(long i) {
                teeSum[0] = teeSum[0] + i;
            }
        }).sum();
        assertEquals(teeSum[0], sum);
    }

    @Test(groups = { "serialization-hostile" })
    public void testForEach() {
        final long[] sum = new long[1];
        LongStream.range(1, 10).filter(new LongPredicate() {
            @Override
            public boolean test(long i) {
                return i % 2 == 0;
            }
        }).forEach(new LongConsumer() {
            @Override
            public void accept(long i) {
                sum[0] = sum[0] + i;
            }
        });
        assertEquals(sum[0], 20);
    }

    @Test(groups = { "serialization-hostile" })
    public void testParForEach() {
        final AtomicLong ai = new AtomicLong(0);
        LongStream.range(1, 10).parallel().filter(new LongPredicate() {
            @Override
            public boolean test(long i) {
                return i % 2 == 0;
            }
        }).forEach((LongConsumer) new LongConsumer() {
            @Override
            public void accept(long l) {
                ai.addAndGet(l);
            }
        });
        assertEquals(ai.get(), 20);
    }

    public void testBox() {
        List<Long> l = LongStream.range(1, 10).parallel().boxed().collect(Collectors.<Long>toList());
        Stream<Long> stream = StreamSupport.stream(l);
        long sum = stream.reduce(0L, new BinaryOperator<Long>() {
            @Override
            public Long apply(Long a, Long b) {
                return a + b;
            }
        });
        assertEquals(sum, 45);
    }

    public void testUnBox() {
        List<Long> l = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        Stream<Long> stream = StreamSupport.stream(l);
        long sum = stream.mapToLong(new ToLongFunction<Long>() {
            @Override
            public long applyAsLong(Long i) {
                return (long) i;
            }
        }).sum();
        assertEquals(sum, 15);
    }

    public void testToArray() {
        {
            // sequential
            long[] array =  LongStream.range(1, 10).map(new LongUnaryOperator() {
                @Override
                public long applyAsLong(long i) {
                    return i * 2;
                }
            }).toArray();
            assertEquals(array, new long[]{2, 4, 6, 8, 10, 12, 14, 16, 18});
        }

        {
            // parallel
            long[] array =  LongStream.range(1, 10).parallel().map(new LongUnaryOperator() {
                @Override
                public long applyAsLong(long i) {
                    return i * 2;
                }
            }).toArray();
            assertEquals(array, new long[]{2, 4, 6, 8, 10, 12, 14, 16, 18});
        }
    }

    public void testSort() {
        final Random r = new Random();
        long[]content = LongStream.generate(new LongSupplier() {
            @Override
            public long getAsLong() {
                return r.nextLong();
            }
        }).limit(10).toArray();
        long[] sortedContent = content.clone();
        Arrays.sort(sortedContent);

        {
            // sequential
            long[] array =  J8Arrays.stream(content).sorted().toArray();
            assertEquals(array, sortedContent);
        }

        {
            // parallel
            long[] array =  J8Arrays.stream(content).parallel().sorted().toArray();
            assertEquals(array, sortedContent);
        }
    }

    public void testSortSort() {
        final Random r = new Random();

        long[] content = LongStream.generate(new LongSupplier() {
            @Override
            public long getAsLong() {
                return r.nextLong();
            }
        }).limit(10).toArray();
        long[] sortedContent = content.clone();
        Arrays.sort(sortedContent);

        {
            long[] array =  J8Arrays.stream(content).sorted().sorted().toArray();
            assertEquals(array, sortedContent);
        }

        {
            long[] array =  J8Arrays.stream(content).parallel().sorted().sorted().toArray();
            assertEquals(array, sortedContent);
        }
    }

    public void testSequential() {

        long[] expected = LongStream.range(1, 1000).toArray();

        class AssertingConsumer implements LongConsumer {
            private final long[] array;
            int offset;

            AssertingConsumer(long[] array) {
                this.array = array;
            }

            @Override
            public void accept(long value) {
                assertEquals(array[offset++], value);
            }

            public int getCount() { return offset; }
        }

        {
            AssertingConsumer consumer = new AssertingConsumer(expected);
            LongStream.range(1, 1000).sequential().forEach(consumer);
            assertEquals(expected.length, consumer.getCount());
        }

        {
            AssertingConsumer consumer = new AssertingConsumer(expected);
            LongStream.range(1, 1000).parallel().sequential().forEach(consumer);
            assertEquals(expected.length, consumer.getCount());
        }
    }

    public void testLimit() {
        long[] expected = LongStream.range(1, 10).toArray();

        {
            long[] actual = LongStream.iterate(1, new LongUnaryOperator() {
                @Override
                public long applyAsLong(long i) {
                    return i + 1;
                }
            }).limit(9).toArray();
            Assert.assertTrue(Arrays.equals(expected, actual));
        }

        {
            long[] actual = LongStream.range(1, 100).parallel().limit(9).toArray();
            Assert.assertTrue(Arrays.equals(expected, actual));
        }
    }
}
