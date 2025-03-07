package com.example.shaker.home

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeListener(
    private var shakeMin: Float = 12f,
    private var onShake: (Float) -> Unit = {}
) : SensorEventListener {
    private var currentAcceleration: Float = 10f
    private var lastAcceleration: Float = SensorManager.GRAVITY_EARTH
    private var acceleration: Float = SensorManager.GRAVITY_EARTH


    override fun onSensorChanged(event: SensorEvent) {
        // Fetching x,y,z values
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        lastAcceleration = currentAcceleration

        // Getting current accelerations
        // with the help of fetched x,y,z values
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta: Float = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta

        if (acceleration > shakeMin) {
            onShake(acceleration)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}