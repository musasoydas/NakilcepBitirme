package com.example.nakilcep.view.successUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nakilcep.adapter.LoadsAdapter
import com.example.nakilcep.databinding.FragmentAllTransportBinding
import com.example.nakilcep.model.Loads
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AllTransport : Fragment() {
    private lateinit var binding: FragmentAllTransportBinding
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var loadList = ArrayList<Loads>()
    private lateinit var recyclerViewLoadsAdapter: LoadsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllTransportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerAdapter()
        val userId = auth.currentUser!!.uid
        database.collection("Post").whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if(value!!.isEmpty)
                {
                    binding.textView7.visibility=View.VISIBLE
                    binding.textView7.text="Henüz Yük İlanınız yoktur"
                }
                if (value != null && !value.isEmpty) {
                    binding.textView7.visibility=View.INVISIBLE
                    val documents = value.documents
                    loadList.clear()
                    for (document in documents) {
                        val loadTitle = document.get("deliveryPoint").toString()
                        val loadingPoint = document.get("loadingPoint").toString()
                        val deliveryPoint = document.get("deliveryPoint").toString()
                        val loadTakeDate = document.get("loadTakeDate").toString()
                        val loadGiveDate = document.get("loadGiveDate").toString()
                        val downloadLoad = Loads(
                            loadTitle,
                            loadingPoint,
                            deliveryPoint,
                            loadTakeDate,
                            loadGiveDate
                        )
                        loadList.add(downloadLoad)
                    }
                    recyclerViewLoadsAdapter.notifyDataSetChanged()//yeni veri geldi haberin olsun!
                }
            }
    }

    private fun recyclerAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        binding.allLoadsRecyclerView.layoutManager = layoutManager
        recyclerViewLoadsAdapter = LoadsAdapter(requireContext(), loadList)
        binding.allLoadsRecyclerView.adapter = recyclerViewLoadsAdapter
    }
}