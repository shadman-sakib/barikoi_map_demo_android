package com.barikoi.mapdemo

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Polyline
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMap.OnPolylineClickListener

class LineMapActivity : AppCompatActivity() {

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
                //after style is set, add line to map
                val line :Polyline =map.addPolyline(PolylineOptions()
                    .addAll(linepoints)  //add all the line points
                    .width(4.0F)    //set width of the line
                    .color(Color.BLUE)     // set color of the line
                )

                //move map camera on the line
                map.moveCamera( CameraUpdateFactory.newLatLngBounds( LatLngBounds.fromLatLngs(line.points), 200))

            }

            //get line click event in map
            map.setOnPolylineClickListener(object: OnPolylineClickListener{
                override fun onPolylineClick(polyline: Polyline) {
                    Toast.makeText(applicationContext, "Line clicked", Toast.LENGTH_LONG).show()
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