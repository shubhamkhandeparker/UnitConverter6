package com.example.unitconverter

data class SpeedUnit(
    val name: String,
    val symbol: String,
    val mpsRatio: Double  // How many meters per second = 1 of this unit
)

val speedUnits = listOf(
    SpeedUnit("Meters per Second", "m/s", 1.0),
    SpeedUnit("Kilometers per Hour", "km/h", 0.277778),
    SpeedUnit("Miles per Hour", "mph", 0.44704),
    SpeedUnit("Feet per Second", "ft/s", 0.3048),
    SpeedUnit("Knots", "kn", 0.514444)
)

fun convertSpeed(value: Double, fromUnit: SpeedUnit, toUnit: SpeedUnit): Double {
    if (value.isNaN() || value.isInfinite()) return 0.0
    val valueInMps = value * fromUnit.mpsRatio
    return valueInMps / toUnit.mpsRatio
}