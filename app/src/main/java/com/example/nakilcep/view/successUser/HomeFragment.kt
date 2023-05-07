package com.example.nakilcep.view.successUser

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nakilcep.databinding.FragmentHomeBinding
import com.example.nakilcep.extensions.showToast
import com.example.nakilcep.view.successUser.HomeFragmentDirections

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

        }
        btnCreatLoadBtnListener()
        btnJobListiningListener()


    }
    fun btnCreatLoadBtnListener(){
        binding.homeFragmentBtnYukOlustur.setOnClickListener {
            val action= HomeFragmentDirections.actionHomeFragmentToCreatLoadFragment(null)
            findNavController().navigate(action)
        }
    }
    fun btnJobListiningListener(){
        binding.homeFragmentBtnGoToJobListing.setOnClickListener {
        val action= HomeFragmentDirections.actionHomeFragmentToJobListingsFragment2()
            findNavController().navigate(action)
        }
    }


}