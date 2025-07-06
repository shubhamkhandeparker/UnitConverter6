package com.example.unitconverter

data class weightUnit(
    val name : String,
    val symbol: String,
    val gramRatio:Double //How many grams =1 of this unit
)

val weightUnits=listOf(
    weightUnit("gram","g",1.0),
    weightUnit("Kilogram","kg",1000.0),
    weightUnit("Pound","lb",453.592),
    weightUnit("Ounce","oz",28.3495),
    weightUnit("Ton","t",1000000.0),

)

fun convertWeight(value:Double,fromUnit: weightUnit,toUnit: weightUnit):Double{
    if(value.isNaN()||value.isInfinite()) return 0.0

    val valueInGram=value*fromUnit.gramRatio
    return valueInGram/toUnit.gramRatio
}