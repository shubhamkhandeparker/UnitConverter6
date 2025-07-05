package com.example.unitconverter

data class LengthUnit(
    val name: String,
    val symbol: String,
    val meterRatio: Double
)

val lengthUnits = listOf(
    LengthUnit("Meter", "m", 1.0),
    LengthUnit("Centimeter", "cm", 0.01),
    LengthUnit("MilliMeter", "mm", 0.001),
    LengthUnit("KiloMeter", "km", 1000.0),
    LengthUnit("Inch", "in", 0.0254),
    LengthUnit("Foot", "ft", 0.3048),
    LengthUnit("Yard", "yd", 0.9144),
    LengthUnit("Mile", "mi", 1609.34),

    )

//Conversion Function
fun convertLength(value: Double,fromUnit: LengthUnit,toUnit: LengthUnit): Double {

    //Convert to meters first , then to target Unit

    val valueInMeters=value*fromUnit.meterRatio
    return valueInMeters/toUnit.meterRatio
}