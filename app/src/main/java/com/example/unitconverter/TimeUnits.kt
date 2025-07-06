package com.example.unitconverter

data class TimeUnit(
    val name: String,
    val symbol: String,
    val secondRatio: Double
)

val timeUnits = listOf(
    TimeUnit("Second", "s", 1.0),
    TimeUnit("Minute", "min", 60.0),
    TimeUnit("Hour", "h", 3600.0),
    TimeUnit("Day", "d", 86400.0),
    TimeUnit("Week", "wk", 604800.0),
    TimeUnit("Month", "mo", 2629746.0),
    TimeUnit("Year", "yr", 31556952.0)
)

fun convertTime(value: Double, fromUnit: TimeUnit, toUnit: TimeUnit): Double {
    if (value.isNaN() || value.isInfinite()) return 0.0
    val valueInSeconds = value * fromUnit.secondRatio
    return valueInSeconds / toUnit.secondRatio
}