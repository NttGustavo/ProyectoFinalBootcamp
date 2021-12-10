package com.example.proyectofinal.UI.MainUI.FragmentsMainUI.Repassword

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
import com.example.proyectofinal.databinding.FragmentRepasswordBinding
import com.google.android.material.snackbar.Snackbar


class RepasswordFragment : Fragment() {

    private lateinit var email: String
    private lateinit var loadingDialog: LoadingDialog

    private val viewModel : RepasswordViewModel by viewModels{
        RepasswordViewModelFactory(requireActivity())
    }


    private var _binding: FragmentRepasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRepasswordBinding.inflate(inflater,container,false)
        val view = binding.root

        loadingDialog = LoadingDialog(requireActivity())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OBSERVERS
        viewModel.isEmailValid.observe(viewLifecycleOwner, Observer(::isEmailValid))
        viewModel.isUserExist.observe(viewLifecycleOwner, Observer(::isUserExist))
        viewModel.isAnswerValid.observe(viewLifecycleOwner, Observer(::isAnswerValid))
        viewModel.isAnswerCorrect.observe(viewLifecycleOwner, Observer(::isAnswerCorrect))
        viewModel.isPasswordValid.observe(viewLifecycleOwner, Observer(::isPasswordValid))
        viewModel.isNewpasswordCorrect.observe(viewLifecycleOwner, Observer(::isChangePassword))

        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::isLoading))


        binding.buttonSendEmailForgotPassword.setOnClickListener{
            email = binding.editTextEmailForgotPassword.text.toString()
            val emailValid = viewModel.ValidarCampoEmail(email)
            if(emailValid){
                viewModel.userExists(email)
            }

        }

        binding.buttonSendVerification.setOnClickListener {
            val answer = binding.editTextRespuestaConfirmadora.text.toString()
            val answerValid = viewModel.ValidarCampoRespuesta(answer)
            if(answerValid){
                viewModel.answerExists(email, answer)
            }
        }

        binding.buttonChangePassword.setOnClickListener {
            val nuevaPassword = binding.editTextNewPassword.text.toString()
            val nuevaPasswordValid = viewModel.ValidarCampoPassword(nuevaPassword)
            if(nuevaPasswordValid){
                viewModel.changePassword(email,nuevaPassword)
            }
        }



    }

    fun isEmailValid(validEmail : Boolean){
        when(validEmail){
            false -> {
                val snackbar = Snackbar.make(binding.root , "Debe ingresar un correo", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    fun isAnswerValid(answerValid: Boolean ){
        when(answerValid){
            false -> {
                val snackbar = Snackbar.make(binding.root , "Debe ingresar una respuesta", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }
    }

    fun isPasswordValid(passwordValid: passwordValid){
        when(passwordValid.valido){
            false -> {
                when(passwordValid.mensaje){
                    "Completar campo" -> {
                        val snackbar = Snackbar.make(binding.root , passwordValid.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                    "La contraseña debe tener al menos 6 caracteres" -> {
                        val snackbar = Snackbar.make(binding.root , passwordValid.mensaje, Snackbar.LENGTH_SHORT)
                        snackbar.show()
                    }
                }
            }
        }
    }

    fun isUserExist(emailPregunta: emailPregunta){
        when(emailPregunta.valido){
            false -> {
                val snackbar = Snackbar.make(binding.root , "No se ha encontrado el usuario", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
            true -> {
                verificacionPaso1(emailPregunta.pregunta)
            }
        }
    }

    fun isAnswerCorrect(isAnswerCorrect : Boolean){
        when(isAnswerCorrect){
            false -> {
                val snackbar = Snackbar.make(binding.root , "La respuesta no coincide", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
            true -> {
                verificacionPaso2()
            }
        }
    }

    fun isChangePassword(isChangePassword : Boolean){
        when(isChangePassword){
            false -> {
                val snackbar = Snackbar.make(binding.root , "No se ha podido cambiar de password", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
            true ->{
                val snackbar = Snackbar.make(binding.root , "Contraseña cambiada exitosamente", Snackbar.LENGTH_SHORT)
                snackbar.show()
                regresarLogin()
            }
        }
    }



    fun verificacionPaso1(pregunta: String){
        binding.linearForgotPasswordEmail.visibility = View.GONE
        binding.linearForgotPasswordPregunta.visibility = View.VISIBLE
        binding.txtQuestionVerifier.text = pregunta
    }

    fun verificacionPaso2(){
        binding.linearForgotPasswordPregunta.visibility = View.GONE
        binding.linearForgotChangePasword.visibility = View.VISIBLE
    }


    fun regresarLogin(){
        findNavController().popBackStack()
    }

    fun isLoading(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

}