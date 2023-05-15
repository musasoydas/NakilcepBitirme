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
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

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

//        binding.textViewdocumentId.text=documentId

        database.collection("Post").whereEqualTo("documentId", documentId)
            .addSnapshotListener { value, error ->
                if (value != null && !value.isEmpty) {
                    val documents = value.documents
                    for (document in documents) {

                        with(binding)
                        {
                            detailFragmentLoadTitle.text=document.get("loadTitle").toString()
                            val downloadUrl=document.get("downloadUrl").toString()
                            detailFragmentPostUser.text=document.get("postUser").toString()
                            detailFragmenDetailDate.text=document.get("detailDate").toString()
                            detailFragmentDocumentId.text=document.get("documentId").toString()
                            detailFragmentLoadTakeDate.text = document.get("loadTakeDate").toString()
                            detailFragmentLoadGiveDate.text = document.get("loadGiveDate").toString()
                            detailFragmentLoadingPoint.text = document.get("loadingPoint").toString()
                            detailFragmentDeliveryPoint.text = document.get("deliveryPoint").toString()
                            detailFragmentLoadGenus.text = document.get("loadGenus").toString()
                            detailFragmentLoadType.text = document.get("loadType").toString()
                            detailFragmentLoadingType.text= document.get("loadingType").toString()
                            detailFragmentLoadExplanation.text = document.get("loadExplanation").toString()
                            Picasso.get().load(downloadUrl).into(binding.detailFragmentImageView)
                        }





                    }


                }
            }
    }

}