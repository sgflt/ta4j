/*
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
package org.ta4j.core.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Position;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BackTestTradingRecord;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.NumFactory;

public class NumberOfPositionsCriterionTest extends AbstractCriterionTest {

  public NumberOfPositionsCriterionTest(final NumFactory numFactory) {
    super(params -> params.length == 0 ? new NumberOfPositionsCriterion()
                                       : new NumberOfPositionsCriterion((boolean) params[0]), numFactory);
  }


  @Test
  public void calculateWithNoPositions() {
    final var series = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(100, 105, 110, 100, 95, 105)
        .build();

    final AnalysisCriterion buyAndHold = getCriterion();
    assertNumEquals(0, buyAndHold.calculate(series, new BackTestTradingRecord()));
  }


  @Test
  public void calculateWithTwoPositions() {
    final var series = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(100, 105, 110, 100, 95, 105)
        .build();
    final TradingRecord tradingRecord = new BackTestTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
        Trade.buyAt(3, series), Trade.sellAt(5, series)
    );

    final AnalysisCriterion buyAndHold = getCriterion();
    assertNumEquals(2, buyAndHold.calculate(series, tradingRecord));
  }


  @Test
  public void calculateWithOnePosition() {
    final var series = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(100, 105, 110, 100, 95, 105)
        .build();
    final Position position = new Position();
    final AnalysisCriterion positionsCriterion = getCriterion();

    assertNumEquals(1, positionsCriterion.calculate(series, position));
  }


  @Test
  public void betterThanWithLessIsBetter() {
    final AnalysisCriterion criterion = getCriterion();
    assertTrue(criterion.betterThan(numOf(3), numOf(6)));
    assertFalse(criterion.betterThan(numOf(7), numOf(4)));
  }


  @Test
  public void betterThanWithLessIsNotBetter() {
    final AnalysisCriterion criterion = getCriterion(false);
    assertFalse(criterion.betterThan(numOf(3), numOf(6)));
    assertTrue(criterion.betterThan(numOf(7), numOf(4)));
  }
}
