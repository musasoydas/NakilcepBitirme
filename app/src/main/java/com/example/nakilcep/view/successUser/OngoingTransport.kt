package com.example.nakilcep.view.successUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nakilcep.R
import com.example.nakilcep.adapter.LoadsAdapter
import com.example.nakilcep.adapter.OngoingAdapter
import com.example.nakilcep.databinding.FragmentCompletedTransportBinding
import com.example.nakilcep.databinding.FragmentOngoingTransportBinding
import com.example.nakilcep.model.Loads
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class OngoingTransport : Fragment() {

    private lateinit var binding: FragmentOngoingTransportBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var loadList = ArrayList<Loads>()
    private lateinit var recyclerViewLoadsAdapter: OngoingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOngoingTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerAdapter()

        val userId = auth.currentUser!!.uid

        database.collection("Post").whereEqualTo("userId", userId)
            .whereEqualTo("postStatus", "ongoing")
            .addSnapshotListener { value, error ->
                if (value!!.isEmpty) {
                    binding.textView7.visibility = View.VISIBLE
                    binding.textView7.text = "Devam Eden Yükünüz Yoktur"
                }
                if (value != null && !value.isEmpty) {
                    binding.textView7.visibility = View.INVISIBLE
                    val documents = value.documents
                    loadList.clear()
                    for (document in documents) {
                        val loadTitle = document.get("deliveryPoint").toString()
                        val loadingPoint = document.get("loadingPoint").toString()
                        val deliveryPoint = document.get("deliveryPoint").toString()
                        val loadTakeDate = document.get("loadTakeDate").toString()
                        val loadGiveDate = document.get("loadGiveDate").toString()
                        val documentId = document.get("documentId").toString()
                        val downloadUrl = document.get("downloadUrl").toString()
                        val downloadLoad = Loads(
                            loadTitle,
                            loadingPoint,
                            deliveryPoint,
                            loadTakeDate,
                            loadGiveDate,
                            documentId,
                            downloadUrl
                        )
                        loadList.add(downloadLoad)
                    }
                    recyclerViewLoadsAdapter.notifyDataSetChanged()//yeni veri geldi haberin olsun!
                }
            }
    }

    private fun recyclerAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        binding.onGoingRecycler.layoutManager = layoutManager
        recyclerViewLoadsAdapter = OngoingAdapter(requireContext(), loadList, ::onAddressClick)
        binding.onGoingRecycler.adapter = recyclerViewLoadsAdapter
    }

    private fun onAddressClick(offerDocumetId: String) {
        database.collection("Post").document(offerDocumetId).update("postStatus", "completed")
        recyclerViewLoadsAdapter.notifyDataSetChanged()
    }
}