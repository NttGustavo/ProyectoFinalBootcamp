package com.example.proyectofinal.Fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectofinal.Firebase.Firebase
import com.example.proyectofinal.Models.Lugar
import com.example.proyectofinal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    var lugares: MutableList<Lugar> = ArrayList()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root

        val firebase = Firebase(requireContext())
        firebase.initFirebase()
        firebase.initLugaresDatabase()
        firebase.getLugares(lugares,binding.recyclerLugares)
        return view
    }




}