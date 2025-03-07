package com.example.shaker.data

//(currently not scaling at all) wrapper class with overloaded operators instead we change implementation from a simple long to work with powers of 10,255, or other stuff like that
class ScalingInt {
    override fun toString(): String {
        return ValueAsString()
    }
    private var value: ULong = 0UL
    fun ValueAsString(): String {
        return value.toString()
    }
    constructor(value: Int) {
        this.value = value.toULong()
    }
    //constructor(value: Long) {
    //    this.value = value.toULong()
    //}
    constructor(value: ULong) {
        this.value = value
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
    operator fun times(other: ScalingInt): ScalingInt {
        return ScalingInt(this.value * other.value)
    }
    operator fun times(other: ULong): ScalingInt {
        return ScalingInt(this.value * other)
    }
    operator fun times(other: Long): ScalingInt {
        return this * other.toULong()
    }
    operator fun times(factor: Float) : ScalingInt{
       return this * factor.toULong()
    }
    operator fun times(factor: Double) : ScalingInt{
        return this * factor.toULong()
    }

    operator fun compareTo(getNextCost: ScalingInt): Int {
        return this.value.compareTo(getNextCost.value)
    }
}