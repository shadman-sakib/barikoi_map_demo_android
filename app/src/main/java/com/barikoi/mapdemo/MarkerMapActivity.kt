package com.barikoi.mapdemo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap

class MarkerMapActivity : AppCompatActivity() {
    private lateinit   var map: MapboxMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_marker_map)


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
            map.setStyle(styleUrl){ style->
                //add amarker in the map
                map.addMarker(
                    MarkerOptions()
                        .setPosition(LatLng(23.8345,90.38044))  //set lat lon position of the marker
                        .setTitle("Map Marker") // set the title of the marker, will show on marker click
                        .setIcon(IconFactory.getInstance(this).fromResource(R.drawable.marker))  //set custom marker image using IconFactory class
                )
            }


            //get marker click event with marker info
            map.setOnMarkerClickListener (object: MapboxMap.OnMarkerClickListener{
                override fun onMarkerClick(marker: Marker): Boolean {
                    Toast.makeText(applicationContext, "clicked on marker", Toast.LENGTH_LONG).show()
                    return true
                }
            })
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