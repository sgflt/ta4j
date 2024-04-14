///**
// * The MIT License (MIT)
// *
// * Copyright (c) 2017-2023 Ta4j Organization & respective
// * authors (see AUTHORS)
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of
// * this software and associated documentation files (the "Software"), to deal in
// * the Software without restriction, including without limitation the rights to
// * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
// * the Software, and to permit persons to whom the Software is furnished to do so,
// * subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
//package org.ta4j.core.indicators.helpers;
//
//import org.ta4j.core.Bar;
//import org.ta4j.core.BarSeries;
//import org.ta4j.core.indicators.AbstractIndicator;
//import org.ta4j.core.num.Num;
//
///**
// * True range indicator.
// *
// * <pre>
// * TrueRange = MAX(high - low, high - previousClose, previousClose - low)
// * </pre>
// */
//public class TRIndicator extends AbstractIndicator<Num> {
//
//    private Bar previousBar;
//
//    /**
//     * Constructor.
//     *
//     * @param series the bar series
//     */
//    public TRIndicator(BarSeries series) {
//        super(series);
//    }
//
//    protected Num calculate() {
//        Bar bar = getBarSeries().getBar();
//        Num high = bar.getHighPrice();
//        Num low = bar.getLowPrice();
//        Num hl = high.minus(low);
//
//        if (previousBar == null) {
//            return hl.abs();
//        }
//
//        Num previousClose = previousBar.getClosePrice();
//        Num hc = high.minus(previousClose);
//        Num cl = previousClose.minus(low);
//        previousBar =  bar;
//        return hl.abs().max(hc.abs()).max(cl.abs());
//
//    }
//
//
//    @Override
//    public Num getValue() {
//        return calculate();
//    }
//
//
//    /** @return {@code 1} */
//    @Override
//    public int getUnstableBars() {
//        return 1;
//    }
//}
