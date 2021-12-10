package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Firebase.FirebaseStorage
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.databinding.FragmentProfileBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {
    private var imageUri: Uri? = null
    lateinit var  loadingDialog: LoadingDialog
    private val viewModel: ProfileViewModel by viewModels{
        ProfileViewModelFactory(requireActivity())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root
        loadingDialog = LoadingDialog(requireActivity())

        recuperarDatos(Comun.currentUser)

        binding.imgFacebook.setOnClickListener{
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.facebook.com/groups/267010254667636")
            startActivity(openURL)
        }

        binding.imgInstagram.setOnClickListener{
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.instagram.com/perutravel/")
            startActivity(openURL)
        }

        binding.imgTwitter.setOnClickListener{
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://twitter.com/TravelCampPeru")
            startActivity(openURL)
        }

        binding.imgYoutube.setOnClickListener{
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.youtube.com/c/VisitPeru")
            startActivity(openURL)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isAvailablePhoto.observe(viewLifecycleOwner, Observer(::isAvailablePhoto))
        viewModel.changePhoto.observe(viewLifecycleOwner, Observer(::isChangePhoto))
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::onLoadingChange))


        binding.cerrarSesion.setOnClickListener{
            salirDelaApp()
        }
        binding.floatingUploadImageButton.setOnClickListener {
            selectImage()

        }

    }

    private fun salirDelaApp() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Saliendo de la app")
            setMessage("Desea salir de la app?")

            setPositiveButton("Yes") { _, _ ->
                borrarDatosGuardados()
                requireActivity().finishAffinity()
            }

            setNegativeButton("No"){alertDialog, _ ->
                alertDialog.dismiss()
            }

            setCancelable(true)
        }.create().show()
    }

    fun onLoadingChange(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

    fun isAvailablePhoto(availabe: Boolean){
        when(availabe){
            false->{
                val snackbar = Snackbar.make(binding.root , "Debe seleccionar una imagen", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    fun isChangePhoto(photo: Photo){
        when(photo.valido){
            false -> {
                val snackbar = Snackbar.make(binding.root , photo.respuesta, Snackbar.LENGTH_SHORT)
                snackbar.show()
                imageUri = null
            }
            true -> {
                val snackbar = Snackbar.make(binding.root , photo.respuesta, Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    private fun borrarDatosGuardados() {
        val sharedPreferences = requireActivity().getSharedPreferences("usuario_guardado", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    private fun recuperarDatos(currentUser: User) {
        binding.txtEmailCurrentUser.text = currentUser.email
        binding.txtNombreCurrentUser.text = currentUser.nombre
        binding.txtNumeroCurrentUser.text = currentUser.telefono
        binding.imgCurrentUser.load(currentUser.imagen)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode  == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
            binding.imgCurrentUser.setImageURI(imageUri)
            viewModel.uploadImage(imageUri)

        }
    }


}