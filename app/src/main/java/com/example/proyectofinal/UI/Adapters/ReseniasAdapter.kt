package com.example.proyectofinal.UI.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.Models.Resenia
import com.example.proyectofinal.databinding.ReseniaItemBinding

class ReseniasAdapter(
    private var resenias: List<Resenia>,
    private val onItemClicked: (Resenia) -> Unit,
    val context: Context): RecyclerView.Adapter<ReseniasAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ReseniaItemBinding): RecyclerView.ViewHolder(binding.root){
        fun render(resenia: Resenia){
            binding.txtLugarResenia.text = resenia.lugar
            binding.txtNombreResenia.text = resenia.nombre
            binding.txtFechaResenia.text = resenia.fecha
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReseniaItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resenia = resenias[position]
        holder.render(resenia)
        holder.itemView.setOnClickListener{
            onItemClicked(resenia)
        }
    }

    override fun getItemCount(): Int {
        return resenias.size
    }

    fun setData(resenias: List<Resenia>){
        this.resenias = resenias
        notifyDataSetChanged()
    }
}