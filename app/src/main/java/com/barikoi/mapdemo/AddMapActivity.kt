package com.barikoi.mapdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap

class AddMapActivity : AppCompatActivity() {
    private lateinit   var map: MapboxMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_add_map)


        //map style name for style link
        val styleId = "osm-liberty"

        // get the api key from barikoi developer panel https://developer.barikoi.com
        val apiKey = getString(R.string.barikoi_api_key)

        // Build the style URL
        val styleUrl = "https://map.barikoi.com/styles/$styleId/style.json?key=$apiKey"



        // Create map view
        mapView= findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.map =map

            // Set the style after mapView was loaded
            map.setStyle(styleUrl)
        }
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