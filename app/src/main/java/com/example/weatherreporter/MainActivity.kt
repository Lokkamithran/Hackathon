package com.example.weatherreporter

import android.Manifest
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





class MainActivity : AppCompatActivity() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var mLatitude: String? = null
    private var mLongitude: String? = null

    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_LOCATION = 199

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sendButton.setOnClickListener{
            //enableLoc()
            getLoc()
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

                // If request is cancelled, the result arrays are empty.
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
    private fun getLoc(){
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION )
            == PackageManager.PERMISSION_DENIED
        ) {
            val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissions(permissions, 1000)
        }
//        mFusedLocationClient!!.lastLocation.addOnSuccessListener {
//            if(it!=null){
//                mLatitude = it.latitude.toString()
//                mLongitude = it.longitude.toString()
//                Toast.makeText(this, "$mLatitude, $mLongitude", Toast.LENGTH_LONG).show()
//                Log.d("mainActivity", "$mLatitude, $mLongitude")
//            }else{
//                Toast.makeText(this, "Error: Location not received:(", Toast.LENGTH_SHORT).show()
//            }
//        }

        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            if(it!=null){
                mLatitude = it.latitude.toString()
                mLongitude = it.longitude.toString()
                Toast.makeText(this, "$mLatitude, $mLongitude", Toast.LENGTH_LONG).show()
                Log.d("mainActivity", "$mLatitude, $mLongitude")
            }else{
                Toast.makeText(this, "Error: Location not received:(", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun enableLoc() {
//        if (googleApiClient == null) {
//            googleApiClient = GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
//                    override fun onConnected(bundle: Bundle?) {}
//                    override fun onConnectionSuspended(i: Int) {
//                        googleApiClient!!.connect()
//                    }
//                })
//                .addOnConnectionFailedListener { connectionResult ->
//                    Log.d(
//                        "Location error",
//                        "Location error " + connectionResult.errorCode
//                    )
//                }.build()
//            googleApiClient!!.connect()
//            val locationRequest = LocationRequest.create()
//            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//            locationRequest.interval = (30 * 1000).toLong()
//            locationRequest.fastestInterval = (5 * 1000).toLong()
//            val builder = LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest)
//            builder.setAlwaysShow(true)
//            val result =
//                LocationServices.SettingsApi.checkLocationSettings(googleApiClient!!, builder.build())
//            result.setResultCallback { result ->
//                val status = result.status
//                when (status.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
//                        status.startResolutionForResult(this, REQUEST_LOCATION)
//
//                        //                                finish();
//                    } catch (e: SendIntentException) {
//                        // Ignore the error.
//                    }
//                }
//            }
//        }
//    }
}