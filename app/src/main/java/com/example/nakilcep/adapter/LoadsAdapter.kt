package com.example.nakilcep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.nakilcep.databinding.TasimalarRecyclerRowBinding
import com.example.nakilcep.model.Loads
import com.example.nakilcep.view.successUser.LoadsFragmentDirections
import com.squareup.picasso.Picasso

class LoadsAdapter(val context: Context, val loadsList: ArrayList<Loads>) :
    RecyclerView.Adapter<LoadsAdapter.LoadsViewHolder>() {
    inner class LoadsViewHolder(val binding: TasimalarRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loads: Loads) {
            with(binding) {
                recyclerRowLoadTitle.text = loads.loadTitle
                recyclerRowFromWhere.text = loads.loadingPoint
                recyclerRowToWhere.text = loads.deliveryPoint
                recyclerRowTakeDate.text = loads.loadTakeDate
                recyclerRowGiveDate.text = loads.loadGiveDate
                Picasso.get().load(loads.downloadUrl).into(recyclerRowImageView)
                recyclerRowCardView.setOnClickListener {
                    val action =
                        LoadsFragmentDirections.actionLoadsFragmentToLoadDetailFragment(loads.documentId)
                    Navigation.findNavController(binding.root).navigate(action)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadsViewHolder {
        val binding =
            TasimalarRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadsViewHolder(binding)
    }
    override fun getItemCount(): Int = loadsList.size
    override fun onBindViewHolder(holder: LoadsViewHolder, position: Int) {
        holder.bind(loadsList[position])

    }
}