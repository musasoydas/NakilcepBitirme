package com.example.nakilcep.view.successUser

import android.os.Binder
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nakilcep.R
import com.example.nakilcep.adapter.LoadsAdapter
import com.example.nakilcep.databinding.FragmentLoadsBinding
import com.example.nakilcep.model.Loads
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoadsFragment : Fragment() {
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var loadsList = ArrayList<Loads>()
    private lateinit var recyclerViewLoadsAdapter: LoadsAdapter

    private lateinit var binding: FragmentLoadsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerAdapter()
        verileriGetir()
//        val userId = auth. currentUser!!.uid

    }

    fun recyclerAdapter() {
        var layoutManager = LinearLayoutManager(requireContext())
        binding.loadsFragmentRecyclerView.layoutManager = layoutManager
        recyclerViewLoadsAdapter = LoadsAdapter(requireContext(), loadsList)
        binding.loadsFragmentRecyclerView.adapter = recyclerViewLoadsAdapter
    }
    fun verileriGetir(){
        database.collection("Post").addSnapshotListener { value, error ->

            if (value != null && !value.isEmpty) {

                val documents = value.documents
                loadsList.clear()
                for (document in documents) {

                    val downloadLoad = Loads(
                        document.get("loadTitle").toString(),
                        document.get("loadingPoint").toString(),
                        document.get("deliveryPoint").toString(),
                        document.get("loadTakeDate").toString(),
                        document.get("loadGiveDate").toString(),
                        document.get("documentId").toString(),
                        document.get("downloadUrl").toString()
                    )
                    loadsList.add(downloadLoad)

                }
                recyclerViewLoadsAdapter.notifyDataSetChanged()//yeni veri geldi haberin olsun!
            }
        }
    }

}