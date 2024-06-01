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
package org.ta4j.core.indicators.bollinger;

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TestUtils.assertNumEquals;

import org.junit.Test;
import org.ta4j.core.indicators.AbstractIndicatorTest;
import org.ta4j.core.indicators.average.SMAIndicator;
import org.ta4j.core.indicators.candles.price.ClosePriceIndicator;
import org.ta4j.core.indicators.candles.price.OpenPriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.mocks.MockBarSeriesBuilder;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.NumFactory;

public class BollingerBandFacadeTest extends AbstractIndicatorTest<Num> {

  public BollingerBandFacadeTest(final NumFactory numFactory) {
    super(numFactory);
  }


  @Test
  public void testCreation() {
    final var data = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2)
        .build();
    final int barCount = 3;

    final var bollingerBandFacade = new BollingerBandFacade(data, barCount, 2);

    assertEquals(data.numFactory(), bollingerBandFacade.bandwidth().getNumFactory());
    assertEquals(data.numFactory(), bollingerBandFacade.middle().getNumFactory());

    final var bollingerBandFacadeOfIndicator = new BollingerBandFacade(new OpenPriceIndicator(data),
        barCount, 2
    );

    assertEquals(data.numFactory(), bollingerBandFacadeOfIndicator.lower().getNumFactory());
    assertEquals(data.numFactory(), bollingerBandFacadeOfIndicator.upper().getNumFactory());
  }


  @Test
  public void testNumericFacadesSameAsDefaultIndicators() {
    final var data = new MockBarSeriesBuilder().withNumFactory(this.numFactory)
        .withData(1, 2, 3, 4, 3, 4, 5, 4, 3, 3, 4, 3, 2)
        .build();
    final var closePriceIndicator = new ClosePriceIndicator(data);
    final int barCount = 3;
    final var sma = new SMAIndicator(closePriceIndicator, 3);

    final var middleBB = new BollingerBandsMiddleIndicator(sma);
    final var standardDeviation = new StandardDeviationIndicator(closePriceIndicator, barCount);
    final var lowerBB = new BollingerBandsLowerIndicator(middleBB, standardDeviation);
    final var upperBB = new BollingerBandsUpperIndicator(middleBB, standardDeviation);
    final var pcb = new PercentBIndicator(new ClosePriceIndicator(data), 5, 2);
    final var widthBB = new BollingerBandWidthIndicator(upperBB, middleBB, lowerBB);

    final var bollingerBandFacade = new BollingerBandFacade(data, barCount, 2);
    final var middleBBNumeric = bollingerBandFacade.middle();
    final var lowerBBNumeric = bollingerBandFacade.lower();
    final var upperBBNumeric = bollingerBandFacade.upper();
    final var widthBBNumeric = bollingerBandFacade.bandwidth();

    final var pcbNumeric = new BollingerBandFacade(data, 5, 2).percentB();

    while (data.advance()) {
      assertNumEquals(pcb.getValue(), pcbNumeric.getValue());
      assertNumEquals(lowerBB.getValue(), lowerBBNumeric.getValue());
      assertNumEquals(middleBB.getValue(), middleBBNumeric.getValue());
      assertNumEquals(upperBB.getValue(), upperBBNumeric.getValue());
      assertNumEquals(widthBB.getValue(), widthBBNumeric.getValue());
    }
  }
}
