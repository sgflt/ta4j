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
package org.ta4j.core.indicators.adx;

import static org.ta4j.core.TestUtils.assertNumEquals;

import java.util.List;

import org.junit.Test;
import org.ta4j.core.MockRule;
import org.ta4j.core.MockStrategy;
import org.ta4j.core.backtest.BacktestBar;
import org.ta4j.core.backtest.BacktestBarSeries;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class PlusDMIndicatorTest extends AbstractIndicatorTest<Num> {

    public PlusDMIndicatorTest(NumFactory numFactory) {
        super(numFactory);
    }

    @Test
    public void zeroDirectionalMovement() {
        var series = new MockBarSeriesBuilder().withNumFactory(numFactory).build();
        var yesterdayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(10).lowPrice(2).build();
        var todayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(6).lowPrice(6).build();
        final var dup = prepareIndicator(series, yesterdayBar, todayBar);

        assertNumEquals(0, dup.getValue());
    }

    private static PlusDMIndicator prepareIndicator(final BacktestBarSeries series, final BacktestBar yesterdayBar,
            final BacktestBar todayBar) {
        var dup = new PlusDMIndicator(series);
        series.replaceStrategy(new MockStrategy(new MockRule(List.of(dup))));
        series.addBar(yesterdayBar);
        series.addBar(todayBar);
        series.advance();
        series.advance();
        return dup;
    }

    @Test
    public void zeroDirectionalMovement2() {
        var series = new MockBarSeriesBuilder().withNumFactory(numFactory).build();
        var yesterdayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(6).lowPrice(12).build();
        var todayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(12).lowPrice(6).build();
        final var dup = prepareIndicator(series, yesterdayBar, todayBar);
        assertNumEquals(0, dup.getValue());
    }

    @Test
    public void zeroDirectionalMovement3() {
        var series = new MockBarSeriesBuilder().withNumFactory(numFactory).build();
        var yesterdayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(6).lowPrice(20).build();
        var todayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(12).lowPrice(4).build();
        final var dup = prepareIndicator(series, yesterdayBar, todayBar);
        assertNumEquals(0, dup.getValue());
    }

    @Test
    public void positiveDirectionalMovement() {
        var series = new MockBarSeriesBuilder().withNumFactory(numFactory).build();
        var yesterdayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(6).lowPrice(6).build();
        var todayBar = series.barBuilder().openPrice(0).closePrice(0).highPrice(12).lowPrice(4).build();
        final var dup = prepareIndicator(series, yesterdayBar, todayBar);
        assertNumEquals(6, dup.getValue());
    }
}
