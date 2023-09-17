package com.barikoi.mapdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.PolygonOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap

class PolygonMapActivity : AppCompatActivity() {
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

        //points list for the polygon
        val polygonpoints: ArrayList<LatLng> = ArrayList()
        polygonpoints.add(LatLng(23.85574361143307, 90.38354443076582))
        polygonpoints.add(LatLng(23.823632508626005,90.40521296373265,))
        polygonpoints.add(LatLng(23.82639837105691,90.42285014172887))
        polygonpoints.add(LatLng(23.86204198543561,90.40050971626783))

        // Create map view
        mapView= findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.map =map

            // Set the style after mapView was loaded
            map.setStyle(styleUrl){ style->
                //after style loaded, then add polygon to map
                val polygon = map.addPolygon(PolygonOptions()
                    .addAll(polygonpoints)  //add all points for the polygon
                    .fillColor(Color.parseColor("#ccebb9")) // you can add fill color and stroke of the polygon from hex string
                    .strokeColor(Color.parseColor("#080808"))
                    .alpha(0.3F) // add transparency to the polygon fill color by setting alpha value from 0.0 to 1.0
                )

                //focus map on the polygon
                map.moveCamera( CameraUpdateFactory.newLatLngBounds( LatLngBounds.fromLatLngs(polygon.points), 50))

            }


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