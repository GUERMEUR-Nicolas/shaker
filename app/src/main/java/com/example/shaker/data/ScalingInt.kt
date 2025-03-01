package com.example.shaker.data

//(currently not scaling at all) wrapper class with overloaded operators instead we change implementation from a simple long to work with powers of 10,255, or other stuff like that
class ScalingInt {
    private var value: Long = 0
    fun ValueAsString(): String {
        return value.toString()
    }

    constructor(value: Long) {
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
    operator fun times(other: Long): ScalingInt {
        return ScalingInt(this.value * other)
    }
    operator fun times(factor: Float) : ScalingInt{
        return ScalingInt((this.value * factor).toLong())
    }
    operator fun times(factor: Double) : ScalingInt{
        return ScalingInt((this.value * factor).toLong())
    }

    operator fun compareTo(getNextCost: ScalingInt): Int {
        return this.value.compareTo(getNextCost.value)
    }
    //fun ValueAsLong() : Long{
    //    return value;
    //}
}