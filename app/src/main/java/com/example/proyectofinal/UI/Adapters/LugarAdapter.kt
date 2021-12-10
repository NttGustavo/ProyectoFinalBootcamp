package com.example.proyectofinal.UI.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.LugarItemBinding

class LugarAdapter(
    private var lugares: List<LugarEntity>,
    private val onItemClicked: (LugarEntity) -> Unit,
    val context: Context): RecyclerView.Adapter<LugarAdapter.ViewHolder>(){


    inner class ViewHolder(val binding: LugarItemBinding): RecyclerView.ViewHolder(binding.root){
        fun render(lugarEntity: LugarEntity){
            binding.txtNombreLugar.text = binding.txtNombreLugar.context.getString(R.string.nombre_lugar, lugarEntity.nombre)
            binding.txtDepartamentoLugar.text = binding.txtDepartamentoLugar.context.getString(R.string.localidad_lugar, lugarEntity.departamento)
            binding.txtDuracionLugar.text = binding.txtDuracionLugar.context.getString(R.string.duracion_lugar, lugarEntity.duracion)
            binding.imgLugar.load(lugarEntity.imagen)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LugarItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lugar = lugares[position]
        holder.render(lugar)
        holder.itemView.setOnClickListener {
            onItemClicked(lugar)
        }
    }

    override fun getItemCount(): Int {
        return lugares.size
    }

    fun setData(lugares: List<LugarEntity>){
        this.lugares = lugares
        notifyDataSetChanged()
    }
}