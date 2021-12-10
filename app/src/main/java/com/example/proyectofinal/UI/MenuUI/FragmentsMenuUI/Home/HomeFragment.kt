package com.example.proyectofinal.UI.MenuUI.FragmentsMenuUI.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyectofinal.UI.Adapters.LugarAdapter
import com.example.proyectofinal.UI.Dialog.LoadingDialog
import com.example.proyectofinal.UI.DetalleUI.LugarActivity
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var adapter: LugarAdapter
    private lateinit var  loadingDialog: LoadingDialog
    private val viewModel: HomeViewModel by viewModels{
        HomeViewModelFactory(requireActivity())
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root

        loadingDialog = LoadingDialog(requireActivity())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerLugares.layoutManager = GridLayoutManager(context,2)
        adapter = LugarAdapter(mutableListOf(),::gotoDescripcionLugar,requireActivity())
        binding.recyclerLugares.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::onLoadingChange))
        viewModel.lugaresList.observe(viewLifecycleOwner, Observer(::onLoadLugares))
        viewModel.loadLugares()
    }

    fun onLoadingChange(isLoading : Boolean){
        when(isLoading){
            true -> loadingDialog.startDialog()
            false -> loadingDialog.dismissDialog()
        }
    }

    fun onLoadLugares(lugares:List<LugarEntity>){
        when(lugares.isNotEmpty()){
            true -> {
                binding.linearVacio.visibility = View.GONE
                adapter.setData(lugares)
            }
            false -> {
                binding.linearVacio.visibility = View.VISIBLE
            }
        }
    }
    fun gotoDescripcionLugar(lugarEntity: LugarEntity){
        val intent = Intent(requireActivity(), LugarActivity::class.java)
        intent.putExtra("lugarEntity",lugarEntity)
        startActivity(intent)
    }

}