package com.example.nakilcep.view.successUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nakilcep.R
import com.example.nakilcep.databinding.BottomSheetLayoutBinding
import com.example.nakilcep.databinding.FragmentLoadDetailBinding
import com.example.nakilcep.databinding.FragmentLoadsBinding
import com.example.nakilcep.extensions.showToast
import com.example.nakilcep.model.Loads
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class LoadDetailFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var documentId: String? = null
    private lateinit var binding: FragmentLoadDetailBinding
    private val args: LoadDetailFragmentArgs by navArgs()
    private lateinit var database: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        documentId = args.documentId



        database.collection("Post").whereEqualTo("documentId", documentId)
            .addSnapshotListener { value, error ->
                if (value != null && !value.isEmpty) {
                    val documents = value.documents
                    for (document in documents) {

                        with(binding)
                        {
                            detailFragmentLoadTitle.text = document.get("loadTitle").toString()
                            val downloadUrl = document.get("downloadUrl").toString()
                            detailFragmentPostUser.text = document.get("postUser").toString()
                            detailFragmenDetailDate.text = document.get("detailDate").toString()
                            detailFragmentDocumentId.text = document.get("documentId").toString()
                            detailFragmentLoadTakeDate.text =
                                document.get("loadTakeDate").toString()
                            detailFragmentLoadGiveDate.text =
                                document.get("loadGiveDate").toString()
                            detailFragmentLoadingPoint.text =
                                document.get("loadingPoint").toString()
                            detailFragmentDeliveryPoint.text =
                                document.get("deliveryPoint").toString()
                            detailFragmentLoadGenus.text = document.get("loadGenus").toString()
                            detailFragmentLoadType.text = document.get("loadType").toString()
                            detailFragmentLoadingType.text = document.get("loadingType").toString()
                            detailFragmentLoadExplanation.text =
                                document.get("loadExplanation").toString()
                            Picasso.get().load(downloadUrl).into(binding.detailFragmentImageView)
                        }
                    }
                }
            }
        btnGiveOfferListener()
    }

    fun btnGiveOfferListener() {

        binding.btnGiveOffer.setOnClickListener {
            val dialoBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(dialoBinding.root)

            with(dialoBinding) {
                btnTeklifVer.setOnClickListener {
                    documentId?.let {
                        val documentRef = database.collection("Post").document(documentId!!)
                        documentRef.get().addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val uuid = auth.currentUser!!.uid
                                val offerPrice = priceText.text.toString().trim()
                                val userData = documentSnapshot.data
                                val postUser = userData?.get("postUser") as String
                                val documentId = userData["documentId"] as String
                                val postUserUUID = userData["userId"] as String
                                val bidderUserEmail = auth.currentUser?.email.toString()
                                val offerMap = hashMapOf(
                                    "postUser" to postUser,
                                    "postUserUUID" to postUserUUID,
                                    "documentId" to documentId,
                                    "bidderUser" to uuid,
                                    "offerPrice" to offerPrice,
                                    "bidderUserEmail" to bidderUserEmail,

                                )


                                val userCollectionRef = database.collection("User")
                                val userDocRef = userCollectionRef.document(postUserUUID)
                                val offerCollectionRef = userDocRef.collection("Offers")
                                val newAdressDocRef = offerCollectionRef.document()
                                newAdressDocRef.set(offerMap).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        requireContext().showToast("Teklif Gönderildi")
                                        dialog.dismiss()
                                    }
                                }.addOnFailureListener {
                                    requireContext().showToast(it.localizedMessage)
                                }

                            }
                        }
                    }
                }
            }
            dialog.show()

        }
    }

}
