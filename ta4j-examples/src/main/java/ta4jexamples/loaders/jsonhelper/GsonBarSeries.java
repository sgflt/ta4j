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
package ta4jexamples.loaders.jsonhelper;

import java.util.LinkedList;
import java.util.List;

import org.ta4j.core.backtest.BacktestBarSeries;
import org.ta4j.core.backtest.BacktestBarSeriesBuilder;

public class GsonBarSeries {

    private String name;
    private final List<GsonBarData> ohlc = new LinkedList<>();

    public static GsonBarSeries from(final BacktestBarSeries series) {
        final var result = new GsonBarSeries();
        result.name = series.getName();
        final var barData = series.getBarData();
        for (final var bar : barData) {
            final GsonBarData exportableBarData = GsonBarData.from(bar);
            result.ohlc.add(exportableBarData);
        }
        return result;
    }

    public BacktestBarSeries toBarSeries() {
        final BacktestBarSeries result = new BacktestBarSeriesBuilder().withName(this.name).build();
        for (final GsonBarData data : this.ohlc) {
            data.addTo(result);
        }
        return result;
    }
}
