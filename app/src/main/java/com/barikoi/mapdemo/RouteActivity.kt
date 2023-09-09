package com.barikoi.mapdemo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.barikoi.mapdemo.adapter.InstructionAdapter
import com.barikoi.mapdemo.model.Instructions
import com.barikoi.mapdemo.model.RouteData
import com.google.gson.Gson
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
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
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import kotlin.math.roundToInt


class RouteActivity : AppCompatActivity() {
    var  queue: RequestQueue? =null
    var apiKey: String? = null
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_route)

        queue = Volley.newRequestQueue(this)

        // barikoi style name
        val styleId = "osm-bright"

        // get the api key from barikoi developer panel https://developer.barikoi.com
        apiKey = getString(R.string.barikoi_api_key)
        // Build the style URL
        val styleUrl = "https://map.barikoi.com/styles/$styleId/style.json?key=$apiKey"

        // Create map view
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
                map.cameraPosition=
                    CameraPosition.Builder().target(LatLng(23.80,90.360)).zoom(12.0).build()
                //set current location pointer in map
                setMapcurrentLocationLayer(map)

                drawRoute(map,
                    LatLng(intent.getDoubleExtra("startlat",0.0), intent.getDoubleExtra("startlon",0.0)),
                        LatLng(intent.getDoubleExtra("endlat",0.0), intent.getDoubleExtra("endlon",0.0))
                )

            }


        }
    }
    //function to call routing API and view route line in map
    private fun drawRoute(map: MapboxMap, start: LatLng, end : LatLng) {
        map.addMarker(MarkerOptions().setPosition(end).setTitle("Destination"))

        var jsonparams: JSONObject = JSONObject()
        jsonparams.put("data", JSONObject())
        jsonparams.getJSONObject("data").put("start", JSONObject())
        jsonparams.getJSONObject("data").put("destination", JSONObject())
        jsonparams.getJSONObject("data").getJSONObject("start").put("latitude", start.latitude)
        jsonparams.getJSONObject("data").getJSONObject("start").put("longitude", start.longitude)
        jsonparams.getJSONObject("data").getJSONObject("destination").put("latitude", end.latitude)
        jsonparams.getJSONObject("data").getJSONObject("destination").put("longitude", end.longitude)
        Log.d("route", jsonparams.toString())

        val url ="https://barikoi.xyz/v2/api/route/gh?api_key=$apiKey&type=json&locale=en-US&elevation=false&profile=foot&ch.disable=true&points_encoded=false"
        Log.d("Route", "Route Url: $url")
        val routeRequest = object: JsonObjectRequest(
            Request.Method.POST, url,
            jsonparams,
            { response ->
                try {
                    val routedata = Gson().fromJson(response.toString(), RouteData::class.java)
                    val points= routedata.paths[0].points?.coordinates?.map { LatLng(it[1], it[0]) }
                    //create a line from route points and view in map
                    val route=map.addPolyline(PolylineOptions().addAll(points).width(4.0F).color(Color.BLUE))

                    //focus map on the line
                    map.moveCamera( CameraUpdateFactory.newLatLngBounds( LatLngBounds.fromLatLngs(route.points), 50))

                    //view route total distance and time
                    routedata.paths[0].distance?.let {  findViewById<TextView>(R.id.textViewTotalDistance).text="Distance: ${it.roundToInt()}m"}
                    routedata.paths[0].time?.let {  findViewById<TextView>(R.id.textViewTotalTime).text= "Duration: ${it/60000} min"}


                    //view instructions
                    generateInstruction(routedata.paths[0].instructions)
                }catch (e : Exception){
                    Log.d("Route error", e.toString())
                }

            },
            { error-> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show() }){
            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject?>? {
                return try {
                    //parse response in utf8 encoding to get proper bangla string
                    val jsonString = String(response.data, Charsets.UTF_8)

                    Response.success<JSONObject?>(
                        JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                } catch (e: UnsupportedEncodingException) {
                    Response.error<JSONObject?>(ParseError(e))
                } catch (je: JSONException) {
                    Response.error<JSONObject?>(ParseError(je))
                }
            }


        }
        queue?.add(routeRequest)
    }

    //fuction to create instructions listview
    private fun generateInstruction(instructions: ArrayList<Instructions>) {
        val instructionView = findViewById<RecyclerView>(R.id.recyclerViewRoute)
        val instructionsAdapter = InstructionAdapter(instructions)
        instructionView.adapter=instructionsAdapter

    }

    //Function to set current location icon layer in map
    private fun setMapcurrentLocationLayer(map: MapboxMap){

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

            val locationComponent = map.locationComponent
            val locationComponentOptions =
                LocationComponentOptions.builder(this@RouteActivity)
                    .pulseEnabled(true)
                    .bearingTintColor(Color.RED)
                    .build()
            map.style?.let {
                val locationComponentActivationOptions =
                    buildLocationComponentActivationOptions(it, locationComponentOptions)
                locationComponent!!.activateLocationComponent(locationComponentActivationOptions)
                locationComponent!!.isLocationComponentEnabled = true
                locationComponent!!.cameraMode = CameraMode.TRACKING_GPS
    //            locationComponent!!.forceLocationUpdate(lastLocation)
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