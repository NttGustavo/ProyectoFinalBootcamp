package com.example.proyectofinal.UI.MainUI.FragmentsMainUI.SignUp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.Models.User
import com.example.proyectofinal.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar


class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels{
        SignupViewModelFactory(requireContext())
    }

    lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(inflater,container,false)
        val view = binding.root
        loadingDialog = LoadingDialog(requireActivity())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ///Observers
        viewModel.isValid.observe(viewLifecycleOwner, Observer(::onNewUserValidChange))
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::onLoadingChange))
        viewModel.isResgister.observe(viewLifecycleOwner, Observer(::onRegisterChange))


        binding.buttonSignup.setOnClickListener {
            val nombre = binding.editTextNombreCompletoSignup.text.toString()
            val email = binding.editTextEmailSignup.text.toString()
            val telefono = binding.editTextPhoneSignup.text.toString()
            val password = binding.editTextPasswordSignup.text.toString()
            val confirmarPassword = binding.editTextConfirmPasswordSignup.text.toString()
            val pregunta = binding.editTextQuestionVerifier.text.toString()
            val respuesta = binding.editTextAnswerVerifier.text.toString()
            val valido = viewModel.ValidarCampos(nombre, email, telefono, password, confirmarPassword, pregunta, respuesta)
            if(valido){
                val newUser = User(nombre,email, telefono, password, pregunta, respuesta, "https://cdn.pixabay.com/photo/2020/07/01/12/58/icon-5359553_960_720.png")
                viewModel.RegisterNewUser(newUser)
            }
        }
    }


    fun onNewUserValidChange(isValidoNewUser: ValidoNewUser){
        when(isValidoNewUser.valido){
            false -> {
                when(isValidoNewUser.mensaje){
                    "Campos incompletos" -> {
                        val snackbar = Snackbar.make(binding.root , isValidoNewUser.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    "Contraseña debe tener al menos 6 caracteres" ->{
                        val snackbar = Snackbar.make(binding.root , isValidoNewUser.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    "Correo invalido" -> {
                        val snackbar = Snackbar.make(binding.root , isValidoNewUser.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    "Las contraseñas no coinciden" -> {
                        val snackbar = Snackbar.make(binding.root , isValidoNewUser.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    "Debe colocar un telefono" -> {
                        val snackbar = Snackbar.make(binding.root , isValidoNewUser.mensaje, Snackbar.LENGTH_SHORT)
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

    fun onRegisterChange(respuestaRegistro: respuestaRegistro){
        when(respuestaRegistro.valido){
            true -> {
                val snackbar = Snackbar.make(binding.root , respuestaRegistro.resultado, Snackbar.LENGTH_SHORT)
                snackbar.show()
                backToSignin()
            }
            false -> {
                val snackbar = Snackbar.make(binding.root , respuestaRegistro.resultado, Snackbar.LENGTH_SHORT)
                snackbar.show()
                Toast.makeText(requireActivity(),respuestaRegistro.resultado,Toast.LENGTH_LONG).show()
            }

        }
    }

    fun backToSignin(){
        findNavController().popBackStack()
    }
}