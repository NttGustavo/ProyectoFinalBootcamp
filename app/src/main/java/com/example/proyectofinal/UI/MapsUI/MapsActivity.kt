package com.example.proyectofinal.UI.MapsUI

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode



class MapsActivity : AppCompatActivity(), OnMapReadyCallback  {

    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory(this)
    }

    private lateinit var locationManager : LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap

    private var mFromLatLng: LatLng? = null
    private var mToLatLng: LatLng? = null

    private var mMarkerFrom: Marker? = null
    private var mMarkerTo: Marker? = null

    companion object{
        private const val FROM_REQUEST_CODE = 1
        private const val TO_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActivity()
        viewModel.polys.observe(this, Observer(::onNewRoute))


        binding.btnNuevoOrigen.setOnClickListener {
            startAutocomplete(FROM_REQUEST_CODE)
        }

        binding.btnZoomOut.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        binding.btnZoomIn.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        binding.btnMyUbication.setOnClickListener {
            requestPermissions()
        }
        /*
        binding.btnTo.setOnClickListener {
            startAutocomplete(TO_REQUEST_CODE)
        }
         */

    }

    private fun setUpActivity() {
        setUpMap()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        Places.initialize(applicationContext, getString(R.string.android_sdk_places_api_key))
    }

    private fun requestPermissions() {

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),111)
        }
        else{
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                obtenerLocalizacion()
            }else{
                Toast.makeText(this, "Debe Activar su GPS", Toast.LENGTH_SHORT).show()
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun obtenerLocalizacion() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if(location != null){
                binding.tvFrom.text = "Tu ubicacion"
                mFromLatLng =  LatLng(location.latitude, location.longitude)
                setMarkerFrom(mFromLatLng!!)
            }
        }
    }

    private fun setMarkerFrom(latLng: LatLng){
        mMarkerFrom?.remove()
        mMarkerFrom =  viewModel.addMarker(mMap,latLng,"Origen")
        viewModel.crearRutas(mFromLatLng!!, mToLatLng!!)
    }

    private fun setMarkerTo(latLng: LatLng){
        mMarkerTo?.remove()
        mMarkerTo =  viewModel.addMarker(mMap,latLng,"Destino")
    }


    private fun setUpMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setMinZoomPreference(2f)
        mMap.setMaxZoomPreference(20f)
        mMap.uiSettings.isMapToolbarEnabled = false
        try {
            val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, com.example.proyectofinal.R.raw.mapstyle))
        }
        catch (e: Resources.NotFoundException) {
        }
        recuperarCoordenadas()
    }

    private fun recuperarCoordenadas(){
        var lugarEntity: LugarEntity = intent.getParcelableExtra("lugarEntity")!!
        binding.tvTo.text = lugarEntity.nombre
        val latitud = lugarEntity.coordenadas.latitud?.toDouble()
        val longitud = lugarEntity.coordenadas.longitud?.toDouble()
        mToLatLng = LatLng(latitud!!, longitud!!)
        setMarkerTo(mToLatLng!!)
    }

    private fun startAutocomplete(requestCode:Int){
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this)
        startActivityForResult(intent,requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == FROM_REQUEST_CODE){
            viewModel.processAutoCompleteResult(resultCode, data){ place ->
                binding.tvFrom.text = place.address
                place.latLng?.let {
                    mFromLatLng = it
                    setMarkerFrom(it)
                }
            }
            return
        }
        else if(requestCode == TO_REQUEST_CODE){
            viewModel.processAutoCompleteResult(resultCode, data){place ->
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

    private fun onNewRoute(polyLineList: List<LatLng> ){
        runOnUiThread {
            viewModel.dibujarNuevaRuta(polyLineList, mMap)
        }
    }

}