package com.example.nakilcep.view.successUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nakilcep.adapter.OfferAdapter
import com.example.nakilcep.databinding.FragmentOffersBinding
import com.example.nakilcep.extensions.showToast
import com.example.nakilcep.model.Offers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OffersFragment : Fragment() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
     var offerList = ArrayList<Offers>()
    private lateinit var recyclerViewOffersAdapter: OfferAdapter


    private lateinit var binding: FragmentOffersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOffersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        verileriAl()
        recyclerAdapter()

    }

    private fun verileriAl() {
        val guncelKullanici = auth.currentUser!!.uid
        database.collection("User").document(guncelKullanici).collection("Offers")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    //hata varsa
                    requireContext().showToast(error.localizedMessage)
                } else {
                    //hata yoksa
                    if (value != null) {
                        //veri var
                        if (!value.isEmpty) {
                            //gelen veri dolu
                            binding.offerErrorText.visibility = View.GONE
                            val documents = value.documents
                            offerList.clear()
                            for (document in documents) {
                                val bidderUserEmail = document.get("bidderUserEmail") as String?
                                val offerPrice = document.get("offerPrice") as String?
                                val documentId = document.get("documentId") as String?
                                database.collection("Post").document(documentId!!).get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot != null && documentSnapshot.exists()) {
                                            val data = documentSnapshot.data
                                            if (data != null) {
                                                val downloadUrl = data["downloadUrl"] as String
                                                val loadTitle = data["loadTitle"] as String
                                                if (downloadUrl != null && loadTitle != null) {
                                                    val downloadOffer = Offers(
                                                        bidderUserEmail,
                                                        offerPrice,
                                                        downloadUrl,
                                                        loadTitle,
                                                        documentId

                                                    )
                                                    offerList.add(downloadOffer)
                                                    recyclerViewOffersAdapter.notifyDataSetChanged()
                                                }
                                            }
                                        }
                                    }
                            }

                        } else {
                            //gelen verinin içi boşsa
                            binding.offerErrorText.visibility = View.VISIBLE
                        }
                    }
                }
            }
    }
    private fun onOfferClick(offerDocumetId: String) {
        database.collection("Post").document(offerDocumetId).update("postStatus","ongoing")
    }
    private fun recyclerAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.offerFragmentRecyclerView.layoutManager = layoutManager
        recyclerViewOffersAdapter = OfferAdapter(offerList,::onOfferClick)
        binding.offerFragmentRecyclerView.adapter = recyclerViewOffersAdapter
    }

}