package com.example.proyectofinal.UI.MainUI.FragmentsMainUI.SignIn

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.Comun.Comun
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.UI.MenuUI.MenuprincipalActivity
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentSigninBinding
import com.google.android.material.snackbar.Snackbar

class SigninFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SigninViewModel by viewModels{
        SinginViewModelFactory(requireActivity())
    }

    lateinit var  loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSigninBinding.inflate(inflater,container,false)
        val view = binding.root
        loadingDialog = LoadingDialog(requireActivity())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///OBSERVERS
        viewModel.isValid.observe(viewLifecycleOwner, Observer(::onUserValidChange))
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::onLoadingChange))
        viewModel.user.observe(viewLifecycleOwner, Observer(::onUserChange))

        viewModel.verficarUsuarioGuardado()

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.navigateToSignupFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.navigateToRepasswordFragment)
        }

        binding.buttonSignin.setOnClickListener {
            val email = binding.editTextEmailSignin.text.toString()
            val password = binding.editTextPasswordSignin.text.toString()
            val valido = viewModel.ValidarCampos(email, password)
            if(valido){
                viewModel.LoginUser(email,password)
            }
        }

    }

    fun onUserValidChange(isValid: Valido){
        when(isValid.valido){
            false -> {
                when(isValid.mensaje){
                    "Campos incompletos"->{
                        val snackbar = Snackbar.make(binding.root , isValid.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    "Correo invalido" ->{
                        val snackbar = Snackbar.make(binding.root , isValid.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                }
            }
        }
    }

    fun onLoadingChange(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

    fun onUserChange(respuestaUser: respuestaUser){
        when(respuestaUser.valido){
            false -> {
                val snackbar = Snackbar.make(binding.root , "Usuario o password incorrectos", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
            true -> {
                val snackbar = Snackbar.make(binding.root , "Hola  ${respuestaUser.user?.nombre}", Snackbar.LENGTH_SHORT)
                snackbar.show()
                respuestaUser.user?.let { user ->
                    viewModel.guardarDatos(user, binding.checkBoxRecordarme.isChecked)
                    gotoActividadPrincipal(user)
                }
            }
        }
    }

    fun gotoActividadPrincipal(currentUser: User) {
        Comun.currentUser = currentUser
        val intent = Intent(requireActivity(), MenuprincipalActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}