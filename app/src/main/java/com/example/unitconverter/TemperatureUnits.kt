package com.example.unitconverter

data class TemperatureUnit(
    val name : String,
    val symbol: String,

)

val temperatureUnits=listOf(
    TemperatureUnit("Celsius","°C"),
    TemperatureUnit(" Fahrenheit","°F"),
    TemperatureUnit("Kelvin","K"),

    )

fun convertTemperature(value:Double,fromUnit: TemperatureUnit,toUnit: TemperatureUnit):Double{

    //Step 1: Convert from any unit to celsius first

    val celsius =when(fromUnit.symbol){
        "°C"->value
        "°F"->(value-32)*5/9
        "K"-> value-273.15
        else -> 0.0
    }

    return when (toUnit.symbol){
        "°C"-> celsius
        "°F"-> celsius *9/5+32
        "K"->celsius +273.15
        else -> 0.0
    }
}
