/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2023 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators.helpers;

import static junit.framework.TestCase.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;
import static org.ta4j.core.num.NaN.NaN;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.MockRule;
import org.ta4j.core.MockStrategy;
import org.ta4j.core.backtest.BacktestBarSeries;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.candles.price.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class LowestValueIndicatorTest extends AbstractIndicatorTest<Num> {

    private BacktestBarSeries data;

    public LowestValueIndicatorTest(final NumFactory numFactory) {
        super(numFactory);
    }

    @Before
    public void setUp() {
        this.data = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
                .withData(1, 2, 3, 4, 3, 4, 5, 6, 4, 3, 2, 4, 3, 1)
                .build();
    }

    @Test
    public void lowestValueIndicatorUsingBarCount5UsingClosePrice() {
        final LowestValueIndicator lowestValue = new LowestValueIndicator(new ClosePriceIndicator(this.data), 5);
        this.data.replaceStrategy(new MockStrategy(new MockRule(List.of(lowestValue))));
        this.data.advance();
        assertNumEquals("1.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("1.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("1.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("1.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("1.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("2.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("3.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("3.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("3.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("3.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("2.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("2.0", lowestValue.getValue());
        this.data.advance();
        assertNumEquals("2.0", lowestValue.getValue());

    }

    @Test
    public void lowestValueIndicatorValueShouldBeEqualsToFirstDataValue() {
        final var lowestValue = new LowestValueIndicator(new ClosePriceIndicator(this.data), 5);
        this.data.replaceStrategy(new MockStrategy(new MockRule(List.of(lowestValue))));

        this.data.advance();
        assertNumEquals("1.0", lowestValue.getValue());
    }

    @Test
    public void lowestValueIndicatorWhenBarCountIsGreaterThanIndex() {
        final var lowestValue = new LowestValueIndicator(new ClosePriceIndicator(this.data), 500);
        this.data.replaceStrategy(new MockStrategy(new MockRule(List.of(lowestValue))));

        for (int i = 0; i < this.data.getBarCount(); i++) {
            this.data.advance();
        }

        assertNumEquals("1.0", lowestValue.getValue());
    }

    @Test
    public void onlyNaNValues() {
        final var series = new MockBarSeriesBuilder().withName("NaN test").withNumFactory(this.numFactory).build();

        for (long i = 0; i < 10000; i++) {
            series.barBuilder().openPrice(NaN).closePrice(NaN).highPrice(NaN).lowPrice(NaN).add();
        }

        final var lowestValue = new LowestValueIndicator(new ClosePriceIndicator(series), 5);
        series.replaceStrategy(new MockStrategy(new MockRule(List.of(lowestValue))));

        while (series.advance()) {
            assertEquals(NaN.toString(), lowestValue.getValue().toString());
        }
    }

    @Test
    public void naNValuesInInterval() {
        final var series = new MockBarSeriesBuilder().withName("NaN test").withNumFactory(this.numFactory).build();
        for (long i = 0; i <= 10; i++) {
            series.barBuilder().openPrice(NaN).closePrice(NaN).highPrice(NaN).lowPrice(NaN).add();
        }

        final var lowestValue = new LowestValueIndicator(new ClosePriceIndicator(series), 2);
        series.replaceStrategy(new MockStrategy(new MockRule(List.of(lowestValue))));

        for (int i = series.getBeginIndex(); i <= series.getEndIndex(); i++) {
            series.advance();
            if (i % 2 != 0) {
                assertEquals(series.getBar(i - 1).closePrice().toString(), lowestValue.getValue().toString());
            } else {
                assertEquals(series.getBar(Math.max(0, i - 1)).closePrice().toString(),
                        lowestValue.getValue().toString());
            }
        }
    }
}
