package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Favoritos

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.UI.Adapters.FavoritosAdapter
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.UI.DetalleUI.LugarViewModel
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.Models.Resenia
import com.example.proyectofinal.UI.DetalleUI.LugarViewModelFactory
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.databinding.FragmentFavoritosBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class FavoritosFragment : Fragment() {

    private lateinit var adapter: FavoritosAdapter
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel : FavoritosViewModel by viewModels{
        FavoritosViewModelFactory(requireActivity())
    }

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritosBinding.inflate(inflater,container,false)
        val view = binding.root
        loadingDialog = LoadingDialog(requireActivity())
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavoritosAdapter(mutableListOf(),requireActivity(),::deleteFavorito,::reseniaFavorito )
        binding.recyclerFavoritos.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::onLoadingChange))
        viewModel.favoritosListLD.observe(viewLifecycleOwner, Observer(::onLoadFavoritos))
        viewModel.isDeleted.observe(viewLifecycleOwner, Observer(::onDeleted))
        viewModel.isReseniaUpload.observe(viewLifecycleOwner, Observer(::onReseniaUpload))
        viewModel.loadFavoritos()
    }

    fun onLoadingChange(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

    fun onDeleted(isDeleted: Boolean){
        when(isDeleted){
            true -> {
                val snackbar = Snackbar.make(binding.root ,"Eliminado", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    fun onLoadFavoritos(favoritos : List<LugarEntity>){
        when(favoritos.isNotEmpty()){
            true -> {
                binding.linearVacio.visibility = View.GONE
                adapter.setData(favoritos)
            }
            false -> {
                binding.linearVacio.visibility = View.VISIBLE
            }
        }
    }

    fun onReseniaUpload(isReseniaUpload : Boolean){
        when(isReseniaUpload){
            true -> {
                val snackbar = Snackbar.make(binding.root ,"Reseña enviada correctamente", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
            false -> {
                val snackbar = Snackbar.make(binding.root ,"Error en el envio", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }
    private fun reseniaFavorito(favorito: LugarEntity) {
        val builder = AlertDialog.Builder(requireContext())
        var editText= EditText(requireContext())
        editText.maxLines = 4
        builder.setView(editText)
        builder.setPositiveButton("Aceptar"){ _ ,_ ->
            val resenia_text = editText.text.toString()
            if(resenia_text.isNotEmpty()){
                val nombre = Comun.currentUser.nombre
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val currentDate = sdf.format(Date())
                val correo = Comun.currentUser.email
                val uid = UUID.randomUUID().toString()
                val resenia = Resenia(nombre,currentDate,resenia_text,correo,favorito.nombre,uid)
                viewModel.addResenia(resenia)
            }
            else{
                val snackbar = Snackbar.make(binding.root ,"Debe escribir una reseña", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }

        builder.setNegativeButton("No"){ _, _ ->

        }

        builder.setTitle("Reseña de ${favorito.nombre}?")
        builder.setMessage("Desea enviar una reseña de ${favorito.nombre}")
        builder.create().show()

    }

    private fun deleteFavorito(favorito: LugarEntity) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Aceptar"){ _ ,_ ->
            viewModel.deleteLugar(favorito)
        }

        builder.setNegativeButton("No"){ _, _ ->

        }

        builder.setTitle("Eliminar ${favorito.nombre}?")
        builder.setMessage("Deseas borrar ${favorito.nombre} de lugares favoritos?")
        builder.create().show()
    }
}