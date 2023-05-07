package com.example.nakilcep.view.unsuccessUser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation

import com.example.nakilcep.databinding.FragmentForgotPasswordBinding
import com.example.nakilcep.view.unsuccessUser.ForgotPasswordDirections
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        giriseDon()
        sifremiUnuttum()

    }

    private fun sifremiUnuttum (){
        with(binding){

            forgetPasswordBtnSifreYenile.setOnClickListener {

                auth.sendPasswordResetEmail(forgetPasswordTxtEmail.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(),"Şifre Yenileme İsteği Gönderildi ", Toast.LENGTH_LONG).show()
                            val action =
                                ForgotPasswordDirections.actionForgotPasswordToLoginScreen()
                            Navigation.findNavController(it).navigate(action)

                        }
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.localizedMessage, android.widget.Toast.LENGTH_LONG).show()
                    }

            }
        }

    }
    private fun giriseDon(){
        with(binding)
        {
            forgetPasswordTxtGirisYap.setOnClickListener {
                val action= ForgotPasswordDirections.actionForgotPasswordToLoginScreen()
                Navigation.findNavController(it).navigate(action)

            }
        }
    }

}