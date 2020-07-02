package com.devculi.embedded.air_quality.entity;

import java.time.Period;

public enum TimeInterval {
    T_1D(Period.ofDays(1)), T_1W(Period.ofWeeks(1)),
    T_1M(Period.ofMonths(1)), T_3M(Period.ofMonths(3)),
    T_1Y(Period.ofYears(1));

    private final Period period;

    TimeInterval(Period period) {
        this.period = period;
    }
}
