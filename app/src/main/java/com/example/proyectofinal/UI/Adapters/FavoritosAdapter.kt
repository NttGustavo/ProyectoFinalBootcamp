package com.example.proyectofinal.UI.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.proyectofinal.Data.Local.Entity.LugarEntity
import com.example.proyectofinal.R
import com.example.proyectofinal.databinding.FavoritosItemBinding

class FavoritosAdapter(
    var favoritosList: List<LugarEntity>,
    var context: Context,
    var onDeleteClicked: (LugarEntity) -> Unit,
    var onEditClicked: (LugarEntity) -> Unit
    ): RecyclerView.Adapter<FavoritosAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FavoritosItemBinding): RecyclerView.ViewHolder(binding.root){

        fun render(favorito: LugarEntity){
            binding.txtNombreLugar.text = binding.txtNombreLugar.context.getString(R.string.nombre_lugar, favorito.nombre)//"${R.string.nombre_lugar} ${lugar.nombre}"
            binding.txtDepartamentoLugar.text = binding.txtDepartamentoLugar.context.getString(R.string.localidad_lugar, favorito.departamento)//"${R.string.localidad_lugar} ${lugar.departamento}"
            binding.txtDuracionLugar.text = binding.txtDuracionLugar.context.getString(R.string.duracion_lugar, favorito.duracion)//"${R.string.duracion_lugar} ${lugar.duracion}"
            binding.imgLugar.load(favorito.imagen)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FavoritosItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(favoritosList[position])
        holder.binding.floatingReseniaButton.setOnClickListener {
            onEditClicked(favoritosList[position])
        }
        holder.binding.floatingDeletingButton.setOnClickListener {
            onDeleteClicked(favoritosList[position])
        }
    }

    override fun getItemCount(): Int {
        return favoritosList.size
    }

    fun setData(favoritos: List<LugarEntity>){
        this.favoritosList = favoritos
        notifyDataSetChanged()
    }
}