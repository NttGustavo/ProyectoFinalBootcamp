package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Resenias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinal.UI.Adapters.ReseniasAdapter
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.Models.Resenia
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FragmentDescriptionsBinding
import org.aviran.cookiebar2.CookieBar

class ReseniasFragment : Fragment() {
    private lateinit var adapter: ReseniasAdapter
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel : ReseniasViewModel by viewModels{
        ReseniasViewModelFactory(requireActivity())
    }

    private var _binding: FragmentDescriptionsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDescriptionsBinding.inflate(inflater,container,false)
        val view = binding.root

        loadingDialog = LoadingDialog(requireActivity())
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.recyclerResenias.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReseniasAdapter(mutableListOf(), ::mostrarResenia,requireActivity())
        binding.recyclerResenias.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::onLoadingChange))
        viewModel.reseniasList.observe(viewLifecycleOwner, Observer(::onLoadResenias))
        viewModel.loadResenias()

    }

    fun onLoadingChange(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

    fun onLoadResenias(resenias : List<Resenia>){
        when(resenias.isNotEmpty()){
            true -> {
                binding.linearVacio.visibility = View.GONE
                adapter.setData(resenias)
            }
            false -> {
                binding.linearVacio.visibility = View.VISIBLE
            }
        }
    }

    private fun mostrarResenia(resenia: Resenia) {
        CookieBar.build(activity)
            .setTitle("Reseña de ${resenia.lugar}")
            .setMessage(resenia.resenia)
            .setIcon(R.drawable.ic_geography)
            .setIconAnimation(R.animator.iconspin)
            .setTitleColor(R.color.black)
            .setActionColor(R.color.contentTextColor)
            .setMessageColor(R.color.black)
            .setBackgroundColor(R.color.quantum_googgreen200)
            .setDuration(5000)
            .setCookiePosition(CookieBar.BOTTOM)
            .show()
    }

}