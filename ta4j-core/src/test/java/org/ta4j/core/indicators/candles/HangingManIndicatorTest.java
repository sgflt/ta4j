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
package org.ta4j.core.indicators.candles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockBarSeries;
import org.ta4j.core.num.Num;

public class HangingManIndicatorTest extends AbstractIndicatorTest<Indicator<Boolean>, Num> {

    private MockBarSeries downtrendSeries;
    private MockBarSeries uptrendSeries;

    public HangingManIndicatorTest(final Function<Number, Num> numFunction) {
        super(numFunction);
    }

    @Before
    public void setUp() {
        final var downtrend = generateDowntrend();
        final var uptrend = generateUptrend();

        final var hangingMan = new MockBar(96d, 90d, 100d, 0d, this.numFunction);
        downtrend.add(hangingMan);
        uptrend.add(hangingMan);

        this.downtrendSeries = new MockBarSeries(downtrend);
        this.uptrendSeries = new MockBarSeries(uptrend);
    }

    private List<Bar> generateDowntrend() {
        final var bars = new ArrayList<Bar>(26);
        for (int i = 26; i > 0; --i) {
            bars.add(new MockBar(i, i, i, i, this.numFunction));
        }

        return bars;
    }

    private List<Bar> generateUptrend() {
        final var bars = new ArrayList<Bar>(26);
        for (int i = 0; i < 26; ++i) {
            bars.add(new MockBar(i, i, i, i, this.numFunction));
        }

        return bars;
    }

    @Test
    public void getValueAsHangingMan() {
        final var hangingManIndicator = new HangingManIndicator(this.uptrendSeries);
        assertTrue(hangingManIndicator.getValue(26));
    }

    @Test
    public void getValueNonHangingMan() {
        final var hangingManIndicator = new HangingManIndicator(this.uptrendSeries);
        assertFalse(hangingManIndicator.getValue(25));
    }

    @Test
    public void getValueHangingManInDowntrend() {
        final var hangingManIndicator = new HangingManIndicator(this.downtrendSeries);
        assertFalse(hangingManIndicator.getValue(26));
    }
}
