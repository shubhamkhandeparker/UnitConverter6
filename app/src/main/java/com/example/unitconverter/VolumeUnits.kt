package com.example.unitconverter

data class VolumeUnit(
    val name: String,
    val symbol:String,
    val literRatio: Double
)

val VolumeUnits=listOf(
    VolumeUnit("Liter","L",1.0),
    VolumeUnit("MilliLiter","ml",0.001),
    VolumeUnit("Gallon","gal",3.78541),
    VolumeUnit("Quart","qt",0.946353),
    VolumeUnit("Pint","pt",0.473176),
    VolumeUnit("Cup","cup",0.236588),
    VolumeUnit("Fluid Ounce","fl",0.0295735)
)

fun convertVolume(value: Double,fromUnit: VolumeUnit,toUnit: VolumeUnit): Double{
    if(value.isNaN()|| value.isInfinite()) return 0.0
    val valueInLiters=value*fromUnit.literRatio
    return valueInLiters /toUnit.literRatio
}