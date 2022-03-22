package com.example.googlemapsgps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.googlemapsgps.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val SECOND = 1000L

    val locationCallbackRequest = LocationRequest.create().apply {
        interval = 10*SECOND
        fastestInterval = 5*SECOND
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val mCurrentLocation = MutableLiveData<LocationModel>()

    private lateinit var locationCallback: LocationCallback

    private val logTAG = MapsActivity::class.java.simpleName

    fun startLocationTracking(context: Context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locations: LocationResult) {
                super.onLocationResult(locations)
                for (location in locations.locations) {
                    mCurrentLocation.postValue(LocationModel(location, Calendar.getInstance().timeInMillis))
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
       // mFusedLocationClient.requestLocationUpdates(locationCallbackRequest, locationCallback, null)
    }

    fun stopLocationTracking() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onPause() {
        super.onPause()
        stopLocationTracking()
    }

    override fun onResume() {
        super.onResume()
        checkPermissionAndSetView()
    }

    private fun checkPermissionAndSetView() {
        if(PermissionHelper.arePermissionsGranted(this)) {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        } else {
            PermissionHelper.askForLocationPermission(this)
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        val tulsa = LatLng(36.1548, -95.9928)
        mMap.addMarker(MarkerOptions().position(tulsa).title("Marker in Tulsa"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tulsa))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PermissionHelper.REQUEST_CODE_FOR_PERMISSION) {
            checkPermissionAndSetView()
        }
    }
}