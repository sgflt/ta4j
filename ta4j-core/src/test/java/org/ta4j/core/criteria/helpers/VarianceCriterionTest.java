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
package org.ta4j.core.criteria.helpers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BackTestTradingRecord;
import org.ta4j.core.criteria.AbstractCriterionTest;
import org.ta4j.core.criteria.pnl.ProfitLossCriterion;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.NumFactory;

public class VarianceCriterionTest extends AbstractCriterionTest {

  public VarianceCriterionTest(final NumFactory numFactory) {
    super(params -> params.length == 2 ? new VarianceCriterion((AnalysisCriterion) params[0], (boolean) params[1])
                                       : new VarianceCriterion((AnalysisCriterion) params[0]), numFactory);
  }


  @Test
  public void calculateVariancePnL() {
    final var series = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(100, 105, 110, 100, 95, 105)
        .build();
    final TradingRecord tradingRecord = new BackTestTradingRecord(Trade.buyAt(0, series, series.numFactory().one()),
        Trade.sellAt(2, series, series.numFactory().one()), Trade.buyAt(3, series, series.numFactory().one()),
        Trade.sellAt(5, series, series.numFactory().one())
    );

    final AnalysisCriterion variance = getCriterion(new ProfitLossCriterion());
    assertNumEquals(6.25, variance.calculate(series, tradingRecord));
  }


  @Test
  public void betterThanWithLessIsBetter() {
    final AnalysisCriterion criterion = getCriterion(new ProfitLossCriterion(), true);
    assertFalse(criterion.betterThan(numOf(5000), numOf(4500)));
    assertTrue(criterion.betterThan(numOf(4500), numOf(5000)));
  }


  @Test
  public void betterThanWithLessIsNotBetter() {
    final AnalysisCriterion criterion = getCriterion(new ProfitLossCriterion());
    assertTrue(criterion.betterThan(numOf(5000), numOf(4500)));
    assertFalse(criterion.betterThan(numOf(4500), numOf(5000)));
  }


  @Test
  public void testCalculateOneOpenPositionShouldReturnZero() {
    this.openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(
        this.numFactory,
        getCriterion(new ProfitLossCriterion()), 0
    );
  }

}
