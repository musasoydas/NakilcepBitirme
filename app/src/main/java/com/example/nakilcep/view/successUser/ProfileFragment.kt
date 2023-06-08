package com.example.nakilcep.view.successUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.nakilcep.databinding.FragmentProfileBinding
import com.example.nakilcep.extensions.showToast
import com.example.nakilcep.model.Loads
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    var paylasimListesi = ArrayList<Loads>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        cikisButonuListener()
        allTransportListener()
        completeTransportListener()
        onGoingTransportListener()
    }


    fun cikisButonuListener() {
        binding.exitCons.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "çıkış yapıldı", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }

    fun allTransportListener() {

        binding.allTransport.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToAllTransport()
            findNavController().navigate(action)
        }
    }

    fun onGoingTransportListener() {

        binding.onGoingCons.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToOngoingTransport()
            findNavController().navigate(action)
        }
    }

    fun completeTransportListener() {

        binding.completedCons.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToCompletedTransport()
            findNavController().navigate(action)
        }
    }

}



