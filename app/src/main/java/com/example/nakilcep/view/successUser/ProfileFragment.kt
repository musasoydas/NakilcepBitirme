package com.example.nakilcep.view.successUser

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nakilcep.R
import com.example.nakilcep.databinding.FragmentNewAddressBinding
import com.example.nakilcep.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        cikisButonuListener()
        allTransportListener()

    }


    fun cikisButonuListener() {
        binding.cikisYap.setOnClickListener {
            auth.signOut()
//            val action = ProfileFragmentDirections.actionProfileFragmentToNavGraph()
//            findNavController().navigate(action)
            Toast.makeText(requireContext(), "çıkış yapıldı", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
        }
    }
    fun allTransportListener(){
        binding.btnAllTransport.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToAllTransport()
            findNavController().navigate(action)
        }
    }

}