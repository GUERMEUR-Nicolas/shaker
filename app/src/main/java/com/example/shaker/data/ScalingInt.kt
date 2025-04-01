package com.example.shaker.data

//(currently not scaling at all) wrapper class with overloaded operators instead we change implementation from a simple long to work with powers of 10,255, or other stuff like that
class ScalingInt {

    //TODO only these 3 methods should be enough to reimplement if we change the backup strucutres, all the others depends on it and never onf the value (type nor value)
    private var value: ULong = 0UL
    fun ValueAsLong(): Long {
        return value.toLong();
    }

    operator fun times(factor: Double): ScalingInt {
        //TODO be careful with rounding errors
        val mult = this.ValueAsLong().toDouble() * factor
        return ScalingInt(mult.toULong())
    }

    constructor(value: ULong)  {
        this.value = value
    }
    //Forwarder converion overload
    fun toLong(): Long {
        return ValueAsLong()
    }

    fun toInt(): Int {
        return ValueAsLong().toInt()
    }

    fun toFloat(): Float {
        return ValueAsLong().toFloat()
    }

    fun ValueAsString(): String {
        return ValueAsLong().toString()
    }

    constructor(value: Int) : this(value.toULong()){
    }
    constructor(double: Double) : this(double.toULong()){
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
        return this * other.ValueAsLong().toDouble()
    }

    operator fun times(other: ULong): ScalingInt {
        return this * other.toDouble()
    }

    operator fun times(other: Long): ScalingInt {
        return this * other.toDouble()
    }

    operator fun times(factor: Float): ScalingInt {
        return this * factor.toDouble()
    }

    operator fun compareTo(getNextCost: ScalingInt): Int {
        return this.value.compareTo(getNextCost.value)
    }

    override fun toString(): String {
        return ValueAsString()
    }
}