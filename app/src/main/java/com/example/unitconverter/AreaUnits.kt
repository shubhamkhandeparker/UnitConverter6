package com.example.unitconverter

data class AreaUnit(
    val name: String,
    val symbol: String,
    val squareMeterRatio: Double  // How many square meters = 1 of this unit
)

val areaUnits = listOf(
    AreaUnit("Square Meter", "m²", 1.0),
    AreaUnit("Square Centimeter", "cm²", 0.0001),
    AreaUnit("Square Kilometer", "km²", 1000000.0),
    AreaUnit("Square Inch", "in²", 0.00064516),
    AreaUnit("Square Foot", "ft²", 0.092903),
    AreaUnit("Square Yard", "yd²", 0.836127),
    AreaUnit("Acre", "ac", 4046.86),
    AreaUnit("Hectare", "ha", 10000.0)
)

fun convertArea(value: Double, fromUnit: AreaUnit, toUnit: AreaUnit): Double {
    if (value.isNaN() || value.isInfinite()) return 0.0
    val valueInSquareMeters = value * fromUnit.squareMeterRatio
    return valueInSquareMeters / toUnit.squareMeterRatio
}