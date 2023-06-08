package com.example.nakilcep.view.successUser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nakilcep.adapter.AddressAdapter
import com.example.nakilcep.databinding.FragmentRegisteredAddressBinding
import com.example.nakilcep.model.Address
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RegisteredAddress : Fragment() {

    private lateinit var binding: FragmentRegisteredAddressBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val args: RegisteredAddressArgs by navArgs()

    private lateinit var recyclerViewAdapter: AddressAdapter
    var listAddress = ArrayList<Address>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisteredAddressBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = requireContext().getSharedPreferences("kontrol", Context.MODE_PRIVATE)
        editor = preferences.edit()

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        recyclerAdapter()
        val uuid = auth.currentUser!!.uid

        database.collection("User").document(uuid).collection("Adress")
            .orderBy(
                "history",
                Query.Direction.DESCENDING
            )//burada  en yeni en üste sıralanacak o düzeni sağladık
            .addSnapshotListener { value, error ->
                if (this@RegisteredAddress.isDetached && error != null) {
                    Toast.makeText(requireContext(), "hata", Toast.LENGTH_SHORT).show()
                } else {
                    if (value != null && !value.isEmpty) {//gelen veri null değil ve içi boş değil ise
                        val documents = value.documents
                        listAddress.clear()
                        for (document in documents) {
                            var myAddress = Address(

                                document.get("currentUser") as String?,//bize any geldipinden cash ettik
                                document.get("authorizedName") as String?,
                                document.get("authorizedPhone") as String?,
                                document.get("addressTitle") as String?,
                                document.get("ProvinceName") as String?,
                                document.get("districtName") as String?,
                                document.get("NeighbourhoodName") as String?,
                                document.get("streetName") as String?,
                                document.get("apartmentNo") as String?,
                                document.get("doorNo") as String?,
                                document.get("openAddress") as String?
                            )
                            listAddress.add(myAddress)
                        }
                        recyclerViewAdapter.notifyDataSetChanged()//yeni veri geldi haberin olsun!
                    }
                }
            }
    }
    private fun onAddressClick(address: String) {
        setFragmentResult(
            "CreateLoad",
            bundleOf(
                "fromFragment" to args.addressType,
                "address" to address
            )
        )
        findNavController().navigateUp()
    }
    private fun recyclerAdapter(){
        var layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = AddressAdapter(requireContext(),listAddress, ::onAddressClick)
        binding.recyclerView.adapter = recyclerViewAdapter
    }
}