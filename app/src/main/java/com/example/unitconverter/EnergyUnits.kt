package com.example.unitconverter

data class EnergyUnit(
    val name: String,
    val symbol: String,
    val jouleRatio: Double  // How many joules = 1 of this unit
)

val energyUnits = listOf(
    EnergyUnit("Joule", "J", 1.0),
    EnergyUnit("Kilojoule", "kJ", 1000.0),
    EnergyUnit("Calorie", "cal", 4.184),
    EnergyUnit("Kilocalorie", "kcal", 4184.0),
    EnergyUnit("Watt Hour", "Wh", 3600.0),
    EnergyUnit("Kilowatt Hour", "kWh", 3600000.0),
    EnergyUnit("BTU", "BTU", 1055.06)
)

fun convertEnergy(value: Double, fromUnit: EnergyUnit, toUnit: EnergyUnit): Double {
    if (value.isNaN() || value.isInfinite()) return 0.0
    val valueInJoules = value * fromUnit.jouleRatio
    return valueInJoules / toUnit.jouleRatio
}