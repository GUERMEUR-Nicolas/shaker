package com.example.shaker.data

import java.math.BigDecimal
import java.math.RoundingMode

//(currently not scaling at all) wrapper class with overloaded operators instead we change implementation from a simple long to work with powers of 10,255, or other stuff like that
class ScalingInt {

    //TODO only these 3 methods should be enough to reimplement if we change the backup strucutres, all the others depends on it and never onf the value (type nor value)
    var value: BigDecimal = BigDecimal("0")
    fun ValueAsLong(): Long {
        return value.toLong();
    }

    operator fun times(factor: Double): ScalingInt {
        //TODO be careful with rounding errors
        val mult = this.ValueAsLong().toDouble() * factor
        return ScalingInt(mult.toULong())
    }

    constructor(value: ULong) {
        this.value = BigDecimal(value.toLong())
    }

    constructor(value: BigDecimal) {
        this.value = value
    }

    //Forwarder converion overload
    fun toLong(): Long {
        return this.value.toLong()
    }

    fun toInt(): Int {
        return this.value.toInt()
    }

    fun toFloat(): Float {
        return this.value.toFloat()
    }

    fun ValueAsString(): String {
        return if (this.value % BigDecimal(1) == BigDecimal(0)) this.value.toBigIntegerExact()
            .toString() else this.value.setScale(1, RoundingMode.FLOOR).toPlainString()
    }

    fun getExponent(): Int {
        return this.value.precision() - this.value.scale() - 1
    }

    constructor(value: Int) : this(BigDecimal(value)) {
    }

    constructor(double: Double) : this(BigDecimal(double)) {
    }

    constructor(value: Float) : this(BigDecimal(value.toDouble())) {
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
        return ValueAsString()
    }
}