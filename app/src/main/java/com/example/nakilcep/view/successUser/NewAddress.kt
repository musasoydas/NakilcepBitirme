package com.example.nakilcep.view.successUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.nakilcep.databinding.FragmentNewAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp

class NewAddress : Fragment() {
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore


    private lateinit var binding: FragmentNewAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAddressBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        btnSaveAddressListener()
    }

    fun btnSaveAddressListener(){

        binding.btnSaveAddress.setOnClickListener {

            // verileri kaydedeceğiz ama bunları anahtar değer eşleştimesi yapmak lazım
            val adressMap = hashMapOf(
                "currentUser" to  auth.currentUser!!.email.toString(),
                "authorizedName" to binding.authorizedName.text.toString().trim(),
                "authorizedPhone" to  binding.authorizedPhone.text.toString().trim(),
                "addressTitle" to  binding.adressTitle.text.toString().trim(),
                "ProvinceName" to  binding.ProvinceName.text.toString().trim(),
                "districtName" to binding.districtName.text.toString().trim(),
                "NeighbourhoodName" to binding.NeighbourhoodName.text.toString().trim(),
                "streetName" to binding.streetName.text.toString().trim(),
                "apartmentNo" to binding.apartmentNo.text.toString().trim(),
                "doorNo" to  binding.doorNo.text.toString().trim(),
                "openAddress" to binding.openAddress.text.toString().trim(),
                "history" to com.google.firebase.Timestamp.now()
            )

            val userID = auth.currentUser!!.uid //giriş yapmış olan kullanıcının UUİD sini getiren kod
            val userCollectionRef=database.collection("User")
            val userDocRef = userCollectionRef.document(userID)
            val adressCollectionRef = userDocRef.collection("Adress")
            val newAdressDocRef = adressCollectionRef.document()
            //yazılan dökümanları kaydetmeye yarıyor
            newAdressDocRef.set(adressMap).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "kayıt başarılı", Toast.LENGTH_SHORT).show()
                    val metin=binding.openAddress.text.toString()
                    val action=NewAddressDirections.actionNewAddressToCreatLoadFragment(metin)
                    findNavController().navigate(action)
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}