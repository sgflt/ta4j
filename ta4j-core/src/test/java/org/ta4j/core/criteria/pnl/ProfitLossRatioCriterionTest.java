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
package org.ta4j.core.criteria.pnl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Test;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Trade;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BackTestTradingRecord;
import org.ta4j.core.criteria.AbstractCriterionTest;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.NumFactory;

public class ProfitLossRatioCriterionTest extends AbstractCriterionTest {

  public ProfitLossRatioCriterionTest(final NumFactory numFactory) {
    super(params -> new ProfitLossRatioCriterion(), numFactory);
  }


  @Test
  public void calculateOnlyWithProfitPositions() {
    final var series = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(120, 70, 100, 130, 105, 120)
        .build();
    final TradingRecord tradingRecord = new BackTestTradingRecord(Trade.buyAt(0, series), Trade.sellAt(2, series),
        Trade.buyAt(3, series), Trade.sellAt(5, series)
    );

    final AnalysisCriterion avtProfit = getCriterion();
    assertNumEquals(0, avtProfit.calculate(series, tradingRecord));
  }


  @Test
  public void calculateOnlyWithLossPositions() {
    final var series =
        new MockBarSeriesBuilder().withNumFactory(this.numFactory).withData(100, 95, 100, 80, 85, 70).build();
    final TradingRecord tradingRecord = new BackTestTradingRecord(Trade.buyAt(0, series), Trade.sellAt(1, series),
        Trade.buyAt(2, series), Trade.sellAt(5, series)
    );

    final AnalysisCriterion avtProfit = getCriterion();
    assertNumEquals(0, avtProfit.calculate(series, tradingRecord));
  }


  @Test
  public void calculateProfitWithShortPositions() {
    final var series =
        new MockBarSeriesBuilder().withNumFactory(this.numFactory).withData(100, 85, 80, 70, 100, 95).build();
    final TradingRecord tradingRecord = new BackTestTradingRecord(Trade.sellAt(0, series), Trade.buyAt(1, series),
        Trade.sellAt(2, series), Trade.buyAt(5, series)
    );

    final AnalysisCriterion avtProfit = getCriterion();
    assertNumEquals(1, avtProfit.calculate(series, tradingRecord));
  }


  @Test
  public void betterThan() {
    final AnalysisCriterion criterion = getCriterion();
    assertTrue(criterion.betterThan(numOf(2.0), numOf(1.5)));
    assertFalse(criterion.betterThan(numOf(1.5), numOf(2.0)));
  }


  @Test
  public void testCalculateOneOpenPositionShouldReturnZero() {
    this.openedPositionUtils.testCalculateOneOpenPositionShouldReturnExpectedValue(this.numFactory, getCriterion(), 0);
  }
}
