package com.example.nakilcep.view.successUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.nakilcep.R
import com.example.nakilcep.databinding.FragmentLoadDetailBinding
import com.example.nakilcep.databinding.FragmentLoadsBinding
import com.example.nakilcep.model.Loads
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class LoadDetailFragment : Fragment() {
    private lateinit var binding: FragmentLoadDetailBinding
    private val args: LoadDetailFragmentArgs by navArgs()
    private lateinit var database: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentLoadDetailBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database=FirebaseFirestore.getInstance()

        val documentId=args.documentId

        var loadDetailList=ArrayList<Loads>()

        binding.textViewdocumentId.text=documentId

        database.collection("Post").whereEqualTo("documentId", documentId)
            .addSnapshotListener { value, error ->
                if (value != null && !value.isEmpty) {
                    val documents = value.documents
                    for (document in documents) {
                       binding.textViewdocumentId.text = document.get("loadTitle").toString()
                        val loadingPoint = document.get("loadingPoint").toString()
                        val deliveryPoint = document.get("deliveryPoint").toString()
                        val loadTakeDate = document.get("loadTakeDate").toString()
                        val loadGiveDate = document.get("loadGiveDate").toString()
                        val documentId=document.get("documentId").toString()

                    }

                }
            }
    }

}