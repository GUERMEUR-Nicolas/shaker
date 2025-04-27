package com.example.shaker.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

//Inspired from https://www.geeksforgeeks.org/how-to-detect-shake-event-in-android/
class Accelerometer {
    private var sensorManager: SensorManager? = null
    private var sensorListener: SensorEventListener? = null

    fun initialize(context: Context, listener: SensorEventListener) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorListener = listener
        subscribe()
    }

    fun subscribe() {
        sensorManager?.registerListener(
            sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun unSubscribe() {
        sensorManager!!.unregisterListener(sensorListener)
    }
}

