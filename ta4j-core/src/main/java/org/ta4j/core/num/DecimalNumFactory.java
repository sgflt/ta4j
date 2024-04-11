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
package org.ta4j.core.num;

import static org.ta4j.core.num.DecimalNum.DEFAULT_PRECISION;

public class DecimalNumFactory implements NumFactory {

    private final DecimalNum minusOne;
    private final DecimalNum zero;
    private final DecimalNum one;
    private final DecimalNum two;
    private final DecimalNum three;
    private final DecimalNum hundred;
    private final DecimalNum thousand;
    private final int precision;

    public DecimalNumFactory() {
       this(DEFAULT_PRECISION);
    }

    public DecimalNumFactory(final int precision) {
        this.precision = precision;
        this.minusOne = DecimalNum.valueOf("-1", precision);
        this.zero = DecimalNum.valueOf("0", precision);
        this.one = DecimalNum.valueOf("1", precision);
        this.two = DecimalNum.valueOf("2", precision);
        this.three = DecimalNum.valueOf("3", precision);
        this.hundred = DecimalNum.valueOf("100", precision);
        this.thousand = DecimalNum.valueOf("1000", precision);
    }


    @Override
    public Num minusOne() {
        return this.minusOne;
    }

    @Override
    public Num zero() {
        return this.zero;
    }

    @Override
    public Num one() {
        return this.one;
    }

    @Override
    public Num two() {
        return this.two;
    }

    @Override
    public Num three() {
        return this.three;
    }

    @Override
    public Num hundred() {
        return this.hundred;
    }

    @Override
    public Num thousand() {
        return this.thousand;
    }

    @Override
    public Num numOf(final Number number) {
        return numOf(number.toString());
    }

    @Override
    public Num numOf(final String number) {
        return DecimalNum.valueOf(number, this.precision);
    }
}
