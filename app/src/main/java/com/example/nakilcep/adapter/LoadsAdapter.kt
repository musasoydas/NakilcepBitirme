package com.example.nakilcep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nakilcep.databinding.TasimalarRecyclerRowBinding
import com.example.nakilcep.model.Loads

class LoadsAdapter(val context:Context,val loadsList:ArrayList<Loads>) : RecyclerView.Adapter<LoadsAdapter.LoadsViewHolder>(){
    inner class LoadsViewHolder(val binding: TasimalarRecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadsViewHolder {
        val binding=TasimalarRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadsViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return loadsList.size
    }

    override fun onBindViewHolder(holder: LoadsViewHolder, position: Int) {
        with(holder.binding)
        {
            recyclerRowLoadTitle.text=loadsList[position].loadTitle
            recyclerRowFromWhere.text="${loadsList[position].loadingPoint}"
            recyclerRowToWhere.text="${loadsList[position].deliveryPoint}"
            recyclerRowTakeDate.text="${loadsList[position].loadTakeDate}"
            recyclerRowGiveDate.text="${loadsList[position].loadGiveDate}"
        }
    }
}