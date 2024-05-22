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
package org.ta4j.core.rules;

import java.time.Instant;

import org.ta4j.core.TradingRecord;

/**
 * Satisfied when the rule has been initialized with {@code true}.
 */
public class BooleanRule extends AbstractRule {

    /** An always-true rule. */
    public static final BooleanRule TRUE = new BooleanRule(true);

    /** An always-false rule. */
    public static final BooleanRule FALSE = new BooleanRule(false);

    private final boolean satisfied;

    /**
     * Constructor.
     *
     * @param satisfied true for the rule to be always satisfied, false to be never
     *                  satisfied
     */
    private BooleanRule(final boolean satisfied) {
        this.satisfied = satisfied;
    }

    /** This rule does not use the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(final TradingRecord tradingRecord) {
        traceIsSatisfied(this.satisfied);
        return this.satisfied;
    }

    @Override
    public void refresh(final Instant tick) {
        // NOOP
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
