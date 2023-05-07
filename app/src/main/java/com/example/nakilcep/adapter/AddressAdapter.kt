package com.example.nakilcep.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nakilcep.databinding.AdressRecyclerRowBinding
import com.example.nakilcep.model.Address

class AddressAdapter(
    val context: Context,
    val listAddress: ArrayList<Address>,
    val onAddressClick: (String) -> Unit
) :RecyclerView.Adapter<AddressAdapter.AddressHolder>() {
    class AddressHolder(val binding: AdressRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressHolder {
        //Oluşturduğumuz tasarımı burada bağlıyoruz
        val binding =
            AdressRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressHolder(binding)
    }

    override fun getItemCount(): Int {
        //oluşturduğumuz recyclerViewde kaç tane satır olacak onun sayısını veriyoruz
        return listAddress.size
    }

    override fun onBindViewHolder(holder: AddressHolder, position: Int) {
        with(holder.binding)
        {
            recyclerRowAddressTitle.text = "Adres Başlığı : " + listAddress[position].adressTitle
            recyclerRowProvinceName.text = "Kayıtlı Şehir: " + listAddress[position].ProvinceName
            recyclerRowOpenAddress.text = "Adres: " + listAddress[position].openAddress

            recyclerRowCardView.setOnClickListener {
                onAddressClick(listAddress[position].openAddress ?: "")
            }
        }
    }
}