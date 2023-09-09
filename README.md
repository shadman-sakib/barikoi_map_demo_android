# Barikoi map android demo

This is an android application demonstrating Barikoi map and  location API integration in android.


### Prerequisites
To use barikoi map and location API, first you need an API key. you can get the [API key here](http://developer.barikoi.com "API key here")

To run the demo app code, put the API key in the string resource xml file of the demo app before building the app from the demo code.

```
<string name="barikoi_api_key">API_KEY_HERE</string>

```

### Get Dependencies
To implement Barikoi maps in android app, it is recommended to use Maplibre android SDK. 
Add bintray Maven repositories to your project-level Gradle file (usually <project>/<app-module>/build.gradle).

```
allprojects {
    repositories {
    ...
    mavenCentral()
    }
}
```

Then add the dependency in your dependencies { ... }:
```
implementation("org.maplibre.gl:android-sdk:10.2.0")
```
### View Map
To add the map, add this code in your activity layout xml file
```
<com.mapbox.mapboxsdk.maps.MapView
        android:id="@+id/mapView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
```

Now in your activity file, add these codes
```
	var apiKey: String? = null
    var map: MapboxMap? = null
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this)
        setContentView(R.layout.activity_map)

		
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

            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
			//omit the default attributions
                map.uiSettings.isAttributionEnabled=false
                map.uiSettings.isLogoEnabled=false
              //set camera position for map
			  map.cameraPosition=CameraPosition.Builder().target(LatLng(23.80,90.360)).zoom(14.0).build()
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
```

### Map Current location
To add the current Location pointer in map, use these codes

```
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
                locationComponent!!.activateLocationComponent(locationComponentActivationOptions)
                locationComponent!!.isLocationComponentEnabled = true
                locationComponent!!.cameraMode = CameraMode.TRACKING_GPS

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
```

### Add a Marker
TO add a marker in map, implement these codes for the map object
```
map.addMarker(MarkerOptions()
                                    .setPosition(LatLng(23.89, 90.38)) //latitude and longitude values
                                    .setTitle("title")
```
to listen to a map marker click event, use this code
```
map.setOnMarkerClickListener (object: MapboxMap.OnMarkerClickListener{
                override fun onMarkerClick(marker: Marker): Boolean {
                   //implement code to consume map marker click event
                    return true
                }

            })
```

### Add a Line
```
val pointss = ArrayList<LatLng>()
//add line points as Latlng objects to the points list
.....


 val line=map.addPolyline(PolylineOptions().addAll(points).width(4.0F).color(Color.BLUE))
//focus map on the line
map.moveCamera( CameraUpdateFactory.newLatLngBounds( LatLngBounds.fromLatLngs(line.points), 50))
```