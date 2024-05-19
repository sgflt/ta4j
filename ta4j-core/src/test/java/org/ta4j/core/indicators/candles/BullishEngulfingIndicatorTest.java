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

import static org.ta4j.core.TestUtils.assertNextFalse;
import static org.ta4j.core.TestUtils.assertNextTrue;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.MockStrategy;
import org.ta4j.core.backtest.BacktestBarSeries;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.Indicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class BullishEngulfingIndicatorTest extends AbstractIndicatorTest<Indicator<Num>, Num> {

  private BacktestBarSeries series;


  public BullishEngulfingIndicatorTest(final NumFactory numFactory) {
    super(numFactory);
  }


  @Before
  public void setUp() {
    this.series = new MockBarSeriesBuilder().withNumFactory(this.numFactory).build();
    this.series.barBuilder().openPrice(10).closePrice(18).highPrice(20).lowPrice(10).add();
    this.series.barBuilder().openPrice(17).closePrice(16).highPrice(19).lowPrice(15).add();
    this.series.barBuilder().openPrice(15).closePrice(18).highPrice(19).lowPrice(14).add();
    this.series.barBuilder().openPrice(15).closePrice(11).highPrice(15).lowPrice(8).add();
    this.series.barBuilder().openPrice(11).closePrice(12).highPrice(12).lowPrice(10).add();
  }


  @Test
  public void getValue() {
    final var bep = new BullishEngulfingIndicator(this.series);
    this.series.replaceStrategy(new MockStrategy(bep));

    assertNextFalse(this.series, bep);
    assertNextFalse(this.series, bep);
    assertNextTrue(this.series, bep);
    assertNextFalse(this.series, bep);
    assertNextFalse(this.series, bep);
  }
}
