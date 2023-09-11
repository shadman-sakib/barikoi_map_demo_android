package com.barikoi.mapdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import barikoi.barikoilocation.BarikoiAPI
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceAPI
import barikoi.barikoilocation.NearbyPlace.NearbyPlaceListener
import barikoi.barikoilocation.PlaceModels.NearbyPlace
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.engine.LocationEngineRequest
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import java.util.ArrayList


class MapActivity : AppCompatActivity() {
    var  queue: RequestQueue? =null
    //Barikoi API key, to get a new key visit https://developer.barikoi.com/
    var apiKey: String? = null

    var map: MapboxMap? = null
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_map)

        queue = Volley.newRequestQueue(this)

        //map style name for style link
        val styleId = "osm-bright"

        // get the api key from barikoi developer panel https://developer.barikoi.com
        apiKey = getString(R.string.barikoi_api_key)

        // Build the style URL
        val styleUrl = "https://map.barikoi.com/styles/$styleId/style.json?key=$apiKey"

        //check location permission, if not granted then request permission
        if (!checkLocationPermission())requestLocationPermission()

        // Create map view
        mapView= findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.map =map
            //omit the default attributions

            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
                map.uiSettings.isAttributionEnabled=false
                map.uiSettings.isLogoEnabled=false
                map.cameraPosition=CameraPosition.Builder().target(LatLng(23.80,90.360)).zoom(14.0).build()
                setMapcurrentLocationLayer()
            }

            //call route activity on marker click
            map.setOnMarkerClickListener (object: MapboxMap.OnMarkerClickListener{
                override fun onMarkerClick(marker: Marker): Boolean {
                    val i = Intent(this@MapActivity, RouteActivity::class.java)
                    i.putExtra("startlat", map.locationComponent.lastKnownLocation?.latitude)
                    i.putExtra("startlon", map.locationComponent.lastKnownLocation?.longitude)
                    i.putExtra("endlat", marker.position.latitude)
                    i.putExtra("endlon",marker.position.longitude)
                    startActivity(i)
                    return true
                }

            })
        }
    }

    //Function to get Nearby Banks/ATMs from Barikoi API and show it on map
    private fun getNearbybanks(map :MapboxMap?) {
        BarikoiAPI.getINSTANCE(this,getString(R.string.barikoi_api_key))

        //check if current location is available in map, if so, call nearby API
        map?.locationComponent?.lastKnownLocation?.let {
            val nearbyapi= NearbyPlaceAPI.builder(this).setLatLng(it.latitude,it.longitude)
                .setDistance(1.0)
                .setType("Bank")
                .build()
            nearbyapi.generateNearbyPlaceListByType ( object: NearbyPlaceListener {
                override fun onPlaceListReceived(places: ArrayList<NearbyPlace>?) {
                    if (places != null) {
                        for( p in places){
                            map.addMarker(
                                MarkerOptions()
                                    .setPosition(
                                        LatLng(
                                            p.latitude,p.longitude
                                        )
                                    )
                                    .setTitle(p.address))
                        }
                    }
                }
                override fun onFailure(message: String?) {
                    TODO("Not yet implemented")
                }

            } )

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
        map?.let {
            val locationComponent = it.locationComponent
            val locationComponentOptions =
                LocationComponentOptions.builder(this@MapActivity)
                    .pulseEnabled(true)
                    .bearingTintColor(Color.RED)
                    .build()
            it.style?.let {
                val locationComponentActivationOptions =
                    buildLocationComponentActivationOptions(it, locationComponentOptions)
                locationComponent.activateLocationComponent(locationComponentActivationOptions)
                locationComponent.isLocationComponentEnabled = true
                locationComponent.cameraMode = CameraMode.TRACKING_GPS

                //after current location is obtained, get nearby bank/ATM in map
                getNearbybanks(map)
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