package com.example.proyectofinal.Mapas

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.proyectofinal.Models.Route
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var latitud: String
    private lateinit var longitud: String

    private var mFromLatLng: LatLng? = null
    private var mToLatLng: LatLng? = null

    private var mMarkerFrom: Marker? = null
    private var mMarkerTo: Marker? = null

    private var polyLineList = mutableListOf<LatLng>()
    private var polylines = mutableListOf<Polyline>()

    companion object{
        private const val KEY = "AIzaSyC1MjBS_hcmSoEjxCmm4JjoAVwsWBmPlco"
        private const val FROM_REQUEST_CODE = 1
        private const val TO_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recuperarCoordenadas()
        setUpMap()
        setUpPlaces()

    }

    private fun getRetrofit(): Retrofit{
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("https://maps.googleapis.com/").build()
        return retrofit
    }



    /*
    private fun drawRoutes(origin: LatLng, destino: LatLng, key: String){
        CoroutineScope(Dispatchers.IO).launch {
            val callback: Response<MapResponse> = getRetrofit().create(MapsApiService::class.java).getRoutes(
                "driving",
                "less_driving",
                "${origin.latitude},${origin.longitude}",
                "${latitud},${longitud}",
                "false",
                key)
            val routes_response: MapResponse? = callback.body()
            runOnUiThread{
                if(callback.isSuccessful){
                    val routeList: List<Route> = routes_response?.routes ?: emptyList()
                    //limpiamos
                    polylines.forEach {
                        it.remove()
                    }
                    polyLineList.clear()
                    //obtenemos nueva ruta
                    routeList.forEach(){route ->
                        val polyline = route.overview_polyline.points
                        polyLineList.addAll(decodePolyline(polyline))

                    }
                    //dibujamos nueva ruta
                    polyLineList.forEachIndexed { index, _ ->
                        if(index < polyLineList.size-1){
                            polylines.add(mMap.addPolyline(PolylineOptions().color(Color.BLUE).width(8f).jointType(JointType.ROUND).add(polyLineList.get(index),polyLineList.get(index+1))))
                        }
                    }


                }
                else{
                    showError()
                }
            }
        }
    }
    */

    private fun showError(){
        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
    }

    private fun setUpPlaces() {
        Places.initialize(applicationContext, getString(R.string.android_sdk_places_api_key))

        binding.btnFrom.setOnClickListener {
            startAutocomplete(FROM_REQUEST_CODE)
        }

        binding.btnTo.setOnClickListener {
            startAutocomplete(TO_REQUEST_CODE)
        }
    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun recuperarCoordenadas(){
        latitud = intent.getStringExtra("LATITUD").toString()
        longitud = intent.getStringExtra("LONGITUD").toString()
    }

    private fun startAutocomplete(requestCode:Int){
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent,requestCode)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == FROM_REQUEST_CODE){
            processAutoCompleteResult(resultCode, data){ place ->
                binding.tvFrom.text = place.address
                place.latLng?.let {
                    mFromLatLng = it
                    setMarkerFrom(it)
                }
            }
            return
        }
        else if(requestCode == TO_REQUEST_CODE){
            processAutoCompleteResult(resultCode, data){place ->
                binding.tvTo.text = place.address
                place.latLng?.let {
                    mToLatLng = it
                    setMarkerTo(it)
                }

            }
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun processAutoCompleteResult(resultCode: Int, data: Intent?, callback: (Place)-> Unit){
        when(resultCode){
            Activity.RESULT_OK ->{
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    Log.d("Place","$place")
                    callback(place)
                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    status.statusMessage?.let { message -> Log.d("Status", message) }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setMinZoomPreference(5f)
        mMap.setMaxZoomPreference(20f)
    }

    private fun addMarker(latLng: LatLng, title:String): Marker {
        val markerOptions = MarkerOptions().position(latLng).title(title)
        val marker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        return marker
    }

    private fun setMarkerFrom(latLng: LatLng){
        mMarkerFrom?.remove()
        mMarkerFrom =  addMarker(latLng,"Desde")
        //crearRutas()
    }

    private fun setMarkerTo(latLng: LatLng){
        mMarkerTo?.remove()
        mMarkerTo =  addMarker(latLng,"Hasta")
        //crearRutas()
    }

    /*
    private fun crearRutas(){
        if(mFromLatLng != null && mToLatLng != null){
            drawRoutes(mFromLatLng!!,mToLatLng!!, KEY)
        }
    }
    */
    fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }
}