package com.example.nakilcep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nakilcep.databinding.OffersRecyclerRowBinding
import com.example.nakilcep.model.Offers
import com.squareup.picasso.Picasso

class OfferAdapter(val offerList: ArrayList<Offers>, val onOfferClick: (String) -> Unit) :
    RecyclerView.Adapter<OfferAdapter.OfferHolder>() {
    inner class OfferHolder(val binding: OffersRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(offers: Offers) {
            with(binding) {
                offerRecyclerRowOfferPrice.text = offers.offerPrice
                offerRecyclerRowBidderUser.text = offers.bidderUserEmail
                offerRecyclerRowLoadTitle.text = offers.loadTitle
                Picasso.get().load(offers.downloadUrl).into(offerRecyclerRowImageView)


                offerRecyclerRowButtonAcceptOffer.setOnClickListener {
                    onOfferClick(offers.documentId!!)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferHolder {
        val binding =
            OffersRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferHolder(binding)
    }
    override fun getItemCount(): Int = offerList.size

    override fun onBindViewHolder(holder: OfferHolder, position: Int) =
        holder.bind(offerList[position])


}