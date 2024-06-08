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
package org.ta4j.core.indicators.aroon;

import static junit.framework.TestCase.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;
import static org.ta4j.core.num.NaN.NaN;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.MockStrategy;
import org.ta4j.core.backtest.BacktestBarSeries;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;

public class AroonUpIndicatorTest {

  private BacktestBarSeries data;


  @Before
  public void init() {
    this.data = new MockBarSeriesBuilder().build();
    this.data.barBuilder().openPrice(168.28).highPrice(169.87).lowPrice(167.15).closePrice(169.64).volume(0).add();
    this.data.barBuilder().openPrice(168.84).highPrice(169.36).lowPrice(168.20).closePrice(168.71).volume(0).add();
    this.data.barBuilder().openPrice(168.88).highPrice(169.29).lowPrice(166.41).closePrice(167.74).volume(0).add();
    this.data.barBuilder().openPrice(168.00).highPrice(168.38).lowPrice(166.18).closePrice(166.32).volume(0).add();
    this.data.barBuilder().openPrice(166.89).highPrice(167.70).lowPrice(166.33).closePrice(167.24).volume(0).add();
    this.data.barBuilder().openPrice(165.25).highPrice(168.43).lowPrice(165.00).closePrice(168.05).volume(0).add();
    this.data.barBuilder().openPrice(168.17).highPrice(170.18).lowPrice(167.63).closePrice(169.92).volume(0).add();
    this.data.barBuilder().openPrice(170.42).highPrice(172.15).lowPrice(170.06).closePrice(171.97).volume(0).add();
    this.data.barBuilder().openPrice(172.41).highPrice(172.92).lowPrice(171.31).closePrice(172.02).volume(0).add();
    this.data.barBuilder().openPrice(171.20).highPrice(172.39).lowPrice(169.55).closePrice(170.72).volume(0).add();
    this.data.barBuilder().openPrice(170.91).highPrice(172.48).lowPrice(169.57).closePrice(172.09).volume(0).add();
    this.data.barBuilder().openPrice(171.80).highPrice(173.31).lowPrice(170.27).closePrice(173.21).volume(0).add();
    this.data.barBuilder().openPrice(173.09).highPrice(173.49).lowPrice(170.80).closePrice(170.95).volume(0).add();
    this.data.barBuilder().openPrice(172.41).highPrice(173.89).lowPrice(172.20).closePrice(173.51).volume(0).add();
    this.data.barBuilder().openPrice(173.87).highPrice(174.17).lowPrice(175.00).closePrice(172.96).volume(0).add();
    this.data.barBuilder().openPrice(173.00).highPrice(173.17).lowPrice(172.06).closePrice(173.05).volume(0).add();
    this.data.barBuilder().openPrice(172.26).highPrice(172.28).lowPrice(170.50).closePrice(170.96).volume(0).add();
    this.data.barBuilder().openPrice(170.88).highPrice(172.34).lowPrice(170.26).closePrice(171.64).volume(0).add();
    this.data.barBuilder().openPrice(171.85).highPrice(172.07).lowPrice(169.34).closePrice(170.01).volume(0).add();
    this.data.barBuilder()
        .openPrice(170.75)
        .highPrice(172.56)
        .lowPrice(170.36)
        .closePrice(172.52)
        .volume(0)
        .add(); // FB,
    // daily,
    // 9.19.'17

  }


  @Test
  public void upAndSlowDown() {
    final var arronUp = new AroonUpIndicator(this.data, 5);
    this.data.replaceStrategy(new MockStrategy(arronUp));

    for (int i = 0; i < 6; i++) {
      this.data.advance();
    }
    assertNumEquals(0, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(80, arronUp.getValue());
    this.data.advance();
    assertNumEquals(60, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(100, arronUp.getValue());
    this.data.advance();
    assertNumEquals(80, arronUp.getValue());
    this.data.advance();
    assertNumEquals(60, arronUp.getValue());
    this.data.advance();
    assertNumEquals(40, arronUp.getValue());
    this.data.advance();
    assertNumEquals(20, arronUp.getValue());
    this.data.advance();
    assertNumEquals(0, arronUp.getValue());

  }


  @Test
  public void onlyNaNValues() {
    final var series = new MockBarSeriesBuilder().withName("NaN test").build();
    for (long i = 0; i <= 1000; i++) {
      series.barBuilder()
          .openPrice(NaN)
          .closePrice(NaN)
          .highPrice(NaN)
          .lowPrice(NaN)
          .volume(NaN)
          .add();
    }

    final var aroonUpIndicator = new AroonUpIndicator(series, 5);
    series.replaceStrategy(new MockStrategy(aroonUpIndicator));

    while (series.advance()) {
      assertEquals(NaN.toString(), aroonUpIndicator.getValue().toString());
    }
  }


  @Test
  public void naNValuesInInterval() {
    final var series = new MockBarSeriesBuilder().withName("NaN test").build();
    for (long i = 0; i <= 10; i++) { // (0, NaN, 2, NaN, 4, NaN, 6, NaN, 8, ...)
      final Num highPrice = i % 2 == 0 ? series.numFactory().numOf(i) : NaN;
      series.barBuilder()
          .openPrice(NaN)
          .closePrice(NaN)
          .highPrice(highPrice)
          .lowPrice(NaN)
          .volume(NaN)
          .add();
    }

    final var aroonUpIndicator = new AroonUpIndicator(series, 5);
    series.replaceStrategy(new MockStrategy(aroonUpIndicator));

    for (int i = series.getBeginIndex(); i <= series.getEndIndex(); i++) {
      series.advance();
      if (i % 2 != 0) {
        assertEquals(NaN.toString(), aroonUpIndicator.getValue().toString());
      } else {
        assertNumEquals(aroonUpIndicator.getValue().toString(), series.numFactory().numOf(100));
      }
    }
  }
}
