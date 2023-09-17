package com.barikoi.mapdemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Polyline
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.engine.LocationEngineRequest
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style

class LocationMapActivity : AppCompatActivity() {

    private lateinit   var map: MapboxMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_location_map)

        //map style name for style link
        val styleId = "osm-liberty"

        // get the api key from barikoi developer panel https://developer.barikoi.com
        val apiKey = getString(R.string.barikoi_api_key)

        // Build the style URL
        val styleUrl = "https://map.barikoi.com/styles/$styleId/style.json?key=$apiKey"

        //Line points array
        val linepoints = ArrayList<LatLng>()
        // Add line points
        linepoints.add(LatLng(23.87397849117633,90.4004025152986,))
        linepoints.add(LatLng(23.860512893207584,90.4004025152986))
        linepoints.add(LatLng(23.837857327354314,90.41815482211757))
        linepoints.add(LatLng(23.82212196615106,90.42035665862238))
        linepoints.add(LatLng(23.812428033815493,90.40370527005587))
        linepoints.add(LatLng(23.762436156214207,90.39462262442248))

        // Create map view
        mapView= findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.map =map

            // Set the style after mapView was loaded
            map.setStyle(styleUrl){style->
                //check if location permissions are allowed
                if(checkLocationPermission()){
                    //if location permission allowed, set current location layer on map
                    setMapcurrentLocationLayer()
                }else{
                    //location permission denied, request permission
                    requestLocationPermission()
                }


            }



        }
    }
    //Function to set current location icon layer in map
    private fun setMapcurrentLocationLayer(){

        //Check if app has location permission , if not, need to add code for permission handling
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.let {
            val locationComponent = it.locationComponent
            val locationComponentOptions =
                LocationComponentOptions.builder(this@LocationMapActivity)
                    .pulseEnabled(true)     // adds pulse effect on current location point
                    .bearingTintColor(Color.RED)
                    .compassAnimationEnabled(true)
                    .build()

            it.style?.let {
                val locationComponentActivationOptions =
                    buildLocationComponentActivationOptions(it, locationComponentOptions)
                locationComponent.activateLocationComponent(locationComponentActivationOptions)
                locationComponent.isLocationComponentEnabled = true
                locationComponent.cameraMode = CameraMode.TRACKING_GPS

            }
        }

    }


    private fun buildLocationComponentActivationOptions(
        style: Style,
        locationComponentOptions: LocationComponentOptions
    ): LocationComponentActivationOptions {
        return LocationComponentActivationOptions
            .builder(this, style)
            .locationComponentOptions(locationComponentOptions)
            .useDefaultLocationEngine(true)
            .locationEngineRequest(
                LocationEngineRequest.Builder(750)
                    .setFastestInterval(750)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .build()
            )
            .build()
    }

    private fun checkLocationPermission(): Boolean{

        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }

    //Function to request location permission
    private fun requestLocationPermission(){
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    //after permission granted, set current location layer in map
                    setMapcurrentLocationLayer()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                    setMapcurrentLocationLayer()
                } else -> {
                // No location access granted.
                Toast.makeText(this, "Location permission denied, cannot get nearby places", Toast.LENGTH_LONG).show()
            }
            }
        }

// ...

// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }


}