package com.example.proyectofinal.UI.MapsUI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Data.Remote.MapsApisRetrofit
import com.example.proyectofinal.Data.RutasRepository
import com.example.proyectofinal.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel(private var context: Context) : ViewModel() {
    private var polyLineList = mutableListOf<LatLng>()
    private val rutasRepository = RutasRepository(context, MapsApisRetrofit.createService())
    private var polylines = mutableListOf<Polyline>()
    val polys = MutableLiveData<List<LatLng>>()

    fun crearRutas(mFromLatLng: LatLng, mToLatLng: LatLng){
        if(mFromLatLng != null && mToLatLng != null){
            drawRoutes(mFromLatLng,mToLatLng)
        }
    }

    private fun drawRoutes(origin: LatLng, destino: LatLng) {
        viewModelScope.launch {
            val routeList = withContext(Dispatchers.IO){
                rutasRepository.getRoutes("walking",
                    "",
                    "${origin.latitude},${origin.longitude}",
                    "${destino.latitude},${destino.longitude}",
                    "false",context.getString(R.string.android_sdk_places_api_key))
            }
            polyLineList.clear()
            routeList.forEach { route ->
                val polyline = route.overview_polyline.points
                polyLineList.addAll(decodePolyline(polyline))
            }
            polys.value = polyLineList
        }
    }


    fun processAutoCompleteResult(resultCode: Int, data: Intent?, callback: (Place)-> Unit){
        when(resultCode){
            Activity.RESULT_OK ->{
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    callback(place)
                }
            }
            AutocompleteActivity.RESULT_ERROR -> {
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                }
            }
        }
    }

    fun addMarker( mMap: GoogleMap, latLng: LatLng, title:String): Marker {
        val markerOptions = MarkerOptions().position(latLng).title(title)
        val marker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
        return marker
    }

    private fun decodePolyline(encoded: String): List<LatLng> {

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
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
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

    fun dibujarNuevaRuta(/*polylines: MutableList<Polyline>,*/ polyLineList: List<LatLng>, mMap: GoogleMap) {
        //limpiamos
        polylines.forEach {
            it.remove()
        }
        //dibujamos nueva ruta
        polyLineList.forEachIndexed { index, _ ->
            if(index < polyLineList.size-1){
                polylines.add(
                    mMap.addPolyline(
                        PolylineOptions()
                            .color(Color.BLUE)
                            .width(8f)
                            .jointType(JointType.ROUND)
                            .add(polyLineList[index],polyLineList[index+1])))
            }
        }
    }

}

class MapsViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}