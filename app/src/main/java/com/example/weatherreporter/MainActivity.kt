package com.example.weatherreporter

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast

import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.os.Handler
import android.os.Looper

import com.google.android.gms.location.LocationSettingsStatusCodes

import com.google.android.gms.location.LocationSettingsResult

import com.google.android.gms.location.LocationSettingsRequest

import com.google.android.gms.location.LocationRequest

import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationCallback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private var resultWeather: Response? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var mLatitude: String = ""
    private var mLongitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLoc()
        val runnable = Runnable {
            if(mLatitude!="" && mLongitude!="")
            retrieveImages()
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 2000)
        sendButton.setOnClickListener{
            getLoc()
            if(mLatitude!="" && mLongitude!="")
                retrieveImages()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1000 -> {

                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLoc()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getLoc() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permissions, 1000)
        }
        mFusedLocationClient!!.lastLocation.addOnSuccessListener {
            if (it != null) {
                mLatitude = it.latitude.toString()
                mLongitude = it.longitude.toString()
                //Toast.makeText(this, "$mLatitude, $mLongitude", Toast.LENGTH_LONG).show()
                Log.d("mainActivity", "$mLatitude, $mLongitude")
            } else {
                Toast.makeText(this, "Error: Location not accessible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun retrieveImages(){
        val mainActivityJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(exception.message)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(android.R.drawable.ic_dialog_alert).show()
            Log.d("mainActivity","wtf: ${exception.message}")
        }
        val coroutineScope = CoroutineScope(mainActivityJob+ Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            resultWeather = WeatherRetriever().getWeather(mLatitude, mLongitude)
            updateUI()
        }
    }
    private fun updateUI(){
        currentlyDataText.text = if(resultWeather!!.weather[0].description!=null)
            (resultWeather!!.weather[0].description?.replaceFirstChar { it.uppercase() })
        else "Wrong Data"
        windDataText.text = if(resultWeather!!.wind.speed.toString()!="")
            ("${resultWeather!!.wind.speed.toString()} km/h")
        else "Wrong Data"
        humidityDataText.text = if(resultWeather!!.main.humidity.toString()!="")
            ("${resultWeather!!.main.humidity.toString()}%")
        else "Wrong Data"
        pressureDataText.text = if(resultWeather!!.main.pressure.toString()!="")
            ("${resultWeather!!.main.pressure.toString()} Pa")
        else "Wrong Data"
        tempDataText.text = if(resultWeather!!.main.temp.toString()!="")
            ("${resultWeather!!.main.temp.toString()} C")
        else "Wrong Data"
        minMaxDataText.text = if(
            resultWeather!!.main.temp_min.toString()!=""
            && resultWeather!!.main.temp_max.toString()!=""
        )
            ("Min: ${resultWeather!!.main.temp_min.toString()} C\n" +
                    "Max: ${resultWeather!!.main.temp_max.toString()} C")
        else "Wrong Data"
        locationDataText.text = if(resultWeather!!.name!=null)
            resultWeather!!.name
        else "Wrong Data"
        latLongDataText.text = if(
            resultWeather!!.coord.lat.toString()!=""
            && resultWeather!!.coord.lon.toString()!=""
        )
            ("Lat: ${resultWeather!!.coord.lat.toString()}\n" +
                    "Lon: ${resultWeather!!.coord.lon.toString()}")
        else "Wrong Data"
        Picasso.get().load(
            "http://openweathermap.org/img/wn/${resultWeather!!.weather[0].icon}@2x.png"
        ).into(weatherIcon)
    }
}