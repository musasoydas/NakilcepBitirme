package com.example.nakilcep.view.successUser

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nakilcep.R
import com.example.nakilcep.databinding.FragmentNewAddressBinding
import com.example.nakilcep.databinding.FragmentProfileBinding
import com.example.nakilcep.extensions.showToast
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth


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
    fun allTransportListener(){

        binding.allTransport.setOnClickListener {
         requireContext().showToast("all tıklandı")


//            val action=ProfileFragmentDirections.actionProfileFragmentToAllTransport()
//            findNavController().navigate(action)
        }
    }
    fun onGoingTransportListener(){

        binding.onGoingCons.setOnClickListener {
            requireContext().showToast("onGoing tıklandı")
//            val action=ProfileFragmentDirections.actionProfileFragmentToAllTransport()
//            findNavController().navigate(action)
        }
    }
    fun completeTransportListener(){

        binding.completedCons.setOnClickListener {
            requireContext().showToast("complete tıklandı")
//            val action=ProfileFragmentDirections.actionProfileFragmentToAllTransport()
//            findNavController().navigate(action)
        }
    }

}