/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2024 Ta4j Organization & respective
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

import static org.ta4j.core.TestUtils.assertNext;
import static org.ta4j.core.TestUtils.assertStable;
import static org.ta4j.core.TestUtils.assertUnstable;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.MockStrategy;
import org.ta4j.core.backtest.BacktestBarSeries;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.numeric.NumericIndicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class GainIndicatorTest extends AbstractIndicatorTest<Num> {

  private BacktestBarSeries data;


  public GainIndicatorTest(final NumFactory numFactory) {
    super(numFactory);
  }


  @Before
  public void setUp() {
    this.data = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(1, 2, 3, 4, 3, 4, 7, 4, 3, 3, 5, 3, 2)
        .build();
  }


  @Test
  public void gainUsingClosePrice() {
    final var gain = NumericIndicator.closePrice(this.data).gain();
    this.data.replaceStrategy(new MockStrategy(gain));

    assertNext(this.data, 0, gain);
    assertUnstable(gain);
    assertNext(this.data, 1, gain);
    assertStable(gain);
    assertNext(this.data, 1, gain);
    assertNext(this.data, 1, gain);
    assertNext(this.data, 0, gain);
    assertNext(this.data, 1, gain);
    assertNext(this.data, 3, gain);
    assertNext(this.data, 0, gain);
    assertNext(this.data, 0, gain);
    assertNext(this.data, 0, gain);
    assertNext(this.data, 2, gain);
    assertNext(this.data, 0, gain);
    assertNext(this.data, 0, gain);
  }
}
