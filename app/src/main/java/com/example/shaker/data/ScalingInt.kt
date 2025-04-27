package com.example.shaker.data

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import kotlin.math.floor

class ScalingInt {

    var value: BigDecimal = BigDecimal("0")
    fun valueAsLong(): Long {
        return value.toLong()
    }

    operator fun times(factor: Double): ScalingInt {
        val mult = this.valueAsLong().toDouble() * factor
        return ScalingInt(mult.toULong())
    }

    constructor(value: ULong) {
        this.value = BigDecimal(value.toLong())
    }

    constructor(value: BigDecimal) {
        this.value = value
    }

    fun toLong(): Long {
        return this.value.toLong()
    }

    fun toInt(): Int {
        return this.value.toInt()
    }

    fun toFloat(): Float {
        return this.value.toFloat()
    }

    fun valueAsString(ignoreFloat: Boolean = false, scale: Int = 1): String {
        return this.softShiftNumber()
            .setScale(if (ignoreFloat) 0 else scale, RoundingMode.HALF_UP)
            .toString() + conwayGuyName(this.getExponent(), true)
    }

    fun getExponent(): Int {
        return this.value.precision() - this.value.scale() - 1
    }


    fun shiftNumber(shift: Int = 3): Int {
        return this.value.movePointLeft(this.getExponent() - this.getExponent() % shift).abs()
            .toInt()
    }

    fun softShiftNumber(shift: Int = 3): BigDecimal {
        return this.value.movePointLeft(this.getExponent() - this.getExponent() % shift).abs()
    }

    constructor(value: Int) : this(BigDecimal(value))
    constructor(double: Double) : this(BigDecimal(double))
    constructor(value: Float) : this(BigDecimal(value.toDouble()))

    constructor(current: String) {
        this.value = BigDecimal(current)
    }

    operator fun plus(other: ScalingInt): ScalingInt {
        return ScalingInt(this.value + other.value)
    }

    operator fun minus(other: ScalingInt): ScalingInt {
        return ScalingInt(this.value - other.value)
    }

    operator fun div(other: ScalingInt): ScalingInt {
        return ScalingInt(this.value / other.value)
    }


    //Forwarder overloads

    operator fun plus(other: Float): ScalingInt {
        return this + ScalingInt(other.toULong())
    }

    operator fun times(other: ScalingInt): ScalingInt {
        return ScalingInt(this.value * other.value)
    }

    operator fun times(other: ULong): ScalingInt {
        return this * ScalingInt(other)
    }

    operator fun times(other: Long): ScalingInt {
        return this * ScalingInt(other.toULong())
    }

    operator fun times(factor: Float): ScalingInt {
        return this * ScalingInt(factor)
    }

    operator fun compareTo(getNextCost: ScalingInt): Int {
        return this.value.compareTo(getNextCost.value)
    }

    override fun toString(): String {
        return valueAsString()
    }
}

fun shortName(name: String, n: Int): String {
    return name[0].titlecase(Locale.ROOT) + if (n == 5 || n == 6) name[1].toString() else ""
}

fun conwayGuyName(exponent: Int, getFirstLetter: Boolean = false, isFinal: Boolean = true): String {
    val firstNames = arrayOf(
        "m", "b", "tr", "quadr", "quint", "sext", "sept", "oct", "non", "dec"
    )
    val genericNames = arrayOf(
        arrayOf("un", "duo", "tre", "quattuor", /*"quinqua"*/"quin", "se", "septe", "octo", "nove"),
        arrayOf(
            "deci",
            "viginti",
            "triginta",
            "quadraginta",
            "quinquaginta",
            "sexaginta",
            "septuaginta",
            "octoginta",
            "nonaginta"
        ),
        arrayOf(
            "centi",
            "ducenti",
            "trecenti",
            "quadringenti",
            "quingenti",
            "sescenti",
            "septigenti",
            "octingenti",
            "nongenti"
        )
    )
    val connections = arrayOf(
        arrayOf("n", "ms", "ns", "ns", "ns", "n", "n", "mx", ""),
        arrayOf("nx", "n", "ns", "ns", "ns", "n", "n", "mx", "")
    )
    val end = if (isFinal) "illion" else "illi"
    val n: Int = if (isFinal) floor((exponent - 3) / 3.0).toInt() else exponent
    if (n >= 11) {
        val strponent = n.toString()
        var rep = ""
        if (n >= 1000) {
            var i = 0
            var length: Int
            var cur: Int
            while (i < strponent.length) {
                length = if (i == 0 && strponent.length % 3 != 0) {
                    strponent.length % 3
                } else {
                    3
                }
                cur = strponent.substring(i, length).toInt()
                rep += if (cur == 0) {
                    if (getFirstLetter) {
                        "n"
                    } else {
                        "n$end"
                    }
                } else {
                    conwayGuyName(cur, getFirstLetter, false)
                }
            }
        } else {
            var nextNonZero: Int = -1
            for (i in 0..<strponent.lastIndex) {
                if (strponent[i] != '0')
                    nextNonZero = i
            }
            for (i in strponent.lastIndex downTo 0) {
                val c: Int = strponent[i].code - '0'.code
                if (c != 0) {
                    if (getFirstLetter) {
                        rep += shortName(genericNames[strponent.lastIndex - i][c - 1], n)
                    } else {
                        rep += genericNames[strponent.lastIndex - i][c - 1]
                        if (i == strponent.lastIndex && nextNonZero != -1) {
                            var connect: String =
                                connections[nextNonZero][strponent[nextNonZero].code - '0'.code - 1]
                            connect = if (c in setOf(3, 6) && ("x" in connect || "s" in connect)) {
                                connect[1].toString()
                            } else if (c in setOf(7, 9) && ("n" in connect || "m" in connect)) {
                                connect[0].toString()
                            } else {
                                ""
                            }
                            rep += connect
                        }
                    }
                }
            }
            if (rep[rep.lastIndex] in setOf('a', 'i'))
                rep = rep.dropLast(1)
            rep += end
        }
        return rep
    } else if (n >= 1 || !isFinal) {
        if (getFirstLetter)
            return shortName(firstNames[n - 1], n)
        return firstNames[n - 1] + end
    } else if (n == 0) {
        if (getFirstLetter)
            return "k"
        return "Thousand"
    }
    return ""
}