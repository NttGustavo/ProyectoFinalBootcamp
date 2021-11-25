package com.example.proyectofinal.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proyectofinal.Models.Lugar
import com.example.proyectofinal.databinding.LugarItemBinding

class LugarAdapter(var lugares: List<Lugar>, val context: Context): RecyclerView.Adapter<LugarAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener
    private lateinit var mLListener: onItemLongClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    interface onItemLongClickListener{
        fun onItemLongClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setOnItemLongClickListener(llistener: onItemLongClickListener){
        mLListener = llistener
    }


    inner class ViewHolder(val binding: LugarItemBinding, listener: onItemClickListener, llistener: onItemLongClickListener): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

            binding.root.setOnLongClickListener {
                llistener.onItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }


        }

        fun render(lugar: Lugar){
            binding.txtNombreLugar.text = lugar.nombre
            binding.txtDepartamentoLugar.text = "Localidad: ${lugar.departamento}"
            binding.txtDuracionLugar.text = "Duración: ${lugar.duracion}"
            binding.imgLugar.load(lugar.imagen)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LugarItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding, mListener, mLListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(lugares[position])
    }

    override fun getItemCount(): Int {
        return lugares.size
    }
}