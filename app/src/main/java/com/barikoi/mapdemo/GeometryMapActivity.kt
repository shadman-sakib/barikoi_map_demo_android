package com.barikoi.mapdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.style.layers.FillLayer
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class GeometryMapActivity : AppCompatActivity() {
    private lateinit   var map: MapboxMap
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_geometry_map)


        //map style name for style link
        val styleId = "osm-liberty"

        // get the api key from barikoi developer panel https://developer.barikoi.com
        val apiKey = getString(R.string.barikoi_api_key)

        // Build the style URL
        val styleUrl = "https://map.barikoi.com/styles/$styleId/style.json?key=$apiKey"


        //geometry geosjon data
        val geometryjson = """
            {
                "type": "Feature",
                "properties": {},
                "geometry":
                {
                  "type": "Polygon",
                  "coordinates": [
                    [
                        [90.4245719514072,23.8498893170947],[90.4162572277404,23.8593578587565],
                        [90.4063700498334,23.8578960563255],[90.406494,23.852892],
                        [90.400035,23.860683],[90.3924952036647,23.8614377480597],
                        [90.3852827038556,23.856767705799],[90.393448,23.845689],
                        [90.3927183589261,23.8419405320778],[90.4014731602744,23.8310647094072],
                        [90.4070952270173,23.8308912076182],[90.4180521159278,23.8383449571236],
                        [90.4223406961995,23.8431574279637],[90.421858,23.846121],
                        [90.4245719514072,23.8498893170947]
                      ]
                    
                  ]
                }
            }
         """
        //create feature object from the Geojson
        val feature = Feature.fromJson(geometryjson)

        // Create map view
        mapView= findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            this.map =map

            // Set the style after mapView was loaded
            map.setStyle(styleUrl){style->

                // Create a GeoJson Source from our feature.
                val geojsonSource = GeoJsonSource("dhaka-airport", feature)
                // Add the source to the style
                style.addSource(geojsonSource)

                // Add layer on map using the Geojson Source
                val layer = LineLayer("dhaka-airport-line", "dhaka-airport")
                    .withProperties(
                        PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                        PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                        PropertyFactory.lineOpacity(1.0f),
                        PropertyFactory.lineWidth(5f),
                        PropertyFactory.lineColor("#0094ff")
                    )

                val filllayer = FillLayer("dhaka-airport-fill", "dhaka-airport")
                    .withProperties(
                        PropertyFactory.fillColor("#009fff"),
                        PropertyFactory.fillOpacity(0.5f)
                    )
                // Add the layer at the end
                style.addLayer(layer)
                style.addLayer(filllayer)
                //set camera on the layer
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng( 23.845689, 90.393448), 14.0))

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