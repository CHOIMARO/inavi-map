package com.example.inavi_map_compose.map.location

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.inavi.mapsdk.maps.LocationProvider
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

public abstract class FusedLocationProvider(private val context: Context) : LocationProvider {

    private val callback = object : FusedLocationCallback(context.applicationContext) {
        override fun onLocationChanged(location: Location?) {
            lastLocation = location
            fireOnLocationChanged()
        }
    }

    private var listener: LocationProvider.OnLocationChangedListener? = null
    private var isListening: Boolean = false
    private var lastLocation: Location? = null

    private val sensorEventListener: FusedSensorEventListener = FusedSensorEventListener(this)
    private var isCompassEnabled: Boolean = false

    public abstract fun hasPermissions(): Boolean
    public abstract fun onPermissionRequest()

    public fun onPermissionGranted() {
        setListening(true)
    }

    override fun activate(listener: LocationProvider.OnLocationChangedListener) {
        this.listener = listener
        if (isListening.not()) {
            if (hasPermissions()) {
                setListening(true)
            } else {
                onPermissionRequest()
            }
        }
    }

    override fun deactivate() {
        if (isListening) {
            setListening(false)
        }
        this.listener = null
    }

    private fun setListening(listening: Boolean) {
        if (listening) {
            callback.startListening()
        } else {
            callback.stopListening()
        }
        isListening = listening
    }

    public fun setCompassEnabled(enabled: Boolean) {
        if (this.isCompassEnabled != enabled) {
            this.isCompassEnabled = enabled
            if (isListening) {
                val context: Context? = context.applicationContext
                if (context != null) {
                    if (enabled) {
                        sensorEventListener.register(context)
                    } else {
                        sensorEventListener.unregister(context)
                        this.bearingDegrees = Float.NaN
                    }
                }
            }
        }
    }

    private var bearingDegrees = Float.NaN

    private fun setBearingDegrees(degrees: Float) {
        if (this.bearingDegrees.isNaN() || abs(this.bearingDegrees - degrees) >= 2f) {
            this.bearingDegrees = degrees
            this.fireOnLocationChanged()
        }
    }

    private fun fireOnLocationChanged() {
        if (this.listener != null && this.lastLocation != null) {
            if (this.bearingDegrees.isNaN().not()) {
                this.lastLocation?.bearing = this.bearingDegrees
            }
            this.listener?.onLocationChanged(this.lastLocation)
        }
    }

    private class FusedSensorEventListener(
        private val locationSource: FusedLocationProvider,
    ) : SensorEventListener {

        private val bearing: BearingCalculator = BearingCalculator()
        private val R: FloatArray = FloatArray(9)
        private val I: FloatArray = FloatArray(9)
        private val values: FloatArray = FloatArray(3)
        private var geomagnetic: FloatArray? = null
        private var gravity: FloatArray? = null

        fun register(context: Context) {
            val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            if (sm != null) {
                sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 1)
                sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 1)
            }
        }

        fun unregister(context: Context) {
            val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            sm?.unregisterListener(this)
            gravity = null
            geomagnetic = null
        }

        override fun onSensorChanged(event: SensorEvent) {
            val sensorType = event.sensor.type
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                var gravity = this.gravity
                if (gravity == null) {
                    gravity = FloatArray(3)
                    this.gravity = gravity
                }
                gravity[0] = event.values[0]
                gravity[1] = event.values[1]
                gravity[2] = event.values[2]
            } else {
                if (sensorType != Sensor.TYPE_MAGNETIC_FIELD) {
                    return
                }
                var geomagnetic = this.geomagnetic
                if (geomagnetic == null) {
                    geomagnetic = FloatArray(3)
                    this.geomagnetic = geomagnetic
                }
                geomagnetic[0] = event.values[0]
                geomagnetic[1] = event.values[1]
                geomagnetic[2] = event.values[2]
            }

            if (gravity != null && geomagnetic != null) {
                if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                    SensorManager.getOrientation(R, values)
                    locationSource.setBearingDegrees(
                        Math.toDegrees(bearing.calculate(values[0])).toFloat()
                    )
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

        private class BearingCalculator {
            private val a: DoubleArray = DoubleArray(40)
            private val b: DoubleArray = DoubleArray(40)
            private var c = 0
            private var d = 0

            fun calculate(azimuth: Float): Double {
                a[d] = cos(azimuth.toDouble())
                b[d] = sin(azimuth.toDouble())
                if (++d == 40) {
                    d = 0
                }
                if (c < 40) {
                    ++c
                }
                var var2 = 0.0
                var var4 = 0.0
                (0 until c).forEach { var6 ->
                    var2 += a[var6]
                    var4 += b[var6]
                }
                return atan2(var4 / c.toDouble(), var2 / c.toDouble())
            }
        }
    }

    private abstract class FusedLocationCallback(private val context: Context) {

        private val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                onLocationChanged(locationResult.lastLocation)
            }
        }

        @SuppressLint("MissingPermission")
        fun startListening() {
            val client = LocationServices.getFusedLocationProviderClient(context)
            GoogleApiAvailability.getInstance()
                .checkApiAvailability(client)
                .onSuccessTask { _ ->
                    val request = LocationRequest
                        .Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                        .setMinUpdateIntervalMillis(1000L)
                        .build()
                    LocationServices.getFusedLocationProviderClient(context)
                        .requestLocationUpdates(request, locationCallback, null)
                }
                .addOnFailureListener {}
        }

        fun stopListening() {
            LocationServices
                .getFusedLocationProviderClient(context)
                .removeLocationUpdates(locationCallback)
        }

        abstract fun onLocationChanged(location: Location?)
    }
}