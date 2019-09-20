package pl.pregiel.workwork.utils

import kotlin.math.roundToLong


fun roundTo2DecimalPoint(double: Double): Double = (double * 100).roundToLong().toDouble() / 100

fun round(double: Double): Double = double.roundToLong().toDouble()