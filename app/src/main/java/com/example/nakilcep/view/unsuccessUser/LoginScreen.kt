package com.example.nakilcep.view.unsuccessUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nakilcep.databinding.FragmentLoginScreenBinding
import com.example.nakilcep.extensions.showToast
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginScreenBinding
    private val args: LoginScreenArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val guncelkullanici = auth.currentUser

        if (!args.fromSignUp && guncelkullanici != null) {
            //önceden girildi ana sayfaya yönlendir.
            val action = LoginScreenDirections.actionLoginScreenToNavYukbul2()
            findNavController().navigate(action)
        }
        setKayitOlClickListener()
        setLogInBtnGirisYapClickListener()
        setSifreUnuttumClickListener()
    }


    fun setKayitOlClickListener() {
        binding.loginBtnKayitOl.setOnClickListener {
            val action = LoginScreenDirections.actionLoginScreenToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }

    fun setSifreUnuttumClickListener() {
        with(binding) {
            sifreunuttum.setOnClickListener {
                val action = LoginScreenDirections.actionLoginScreenToForgotPassword()
                Navigation.findNavController(it).navigate(action)

            }
        }
    }

    fun setLogInBtnGirisYapClickListener() {
        with(binding) {

            loginBtnGirisYap.setOnClickListener {
                if (loginEmailText.text.isNotEmpty() && loginPasswordText.text.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(
                        loginEmailText.text.toString(),
                        loginPasswordText.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Ana Sayfaya Gidecektir
                                val guncelkullanici = auth.currentUser?.email.toString()

                                requireContext().showToast("başarılı ${guncelkullanici} ")

                                val action = LoginScreenDirections.actionLoginScreenToNavYukbul2()
                                Navigation.findNavController(it).navigate(action)

                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                it.localizedMessage,
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    requireContext().showToast("Email veya Password Giriniz")
                }
            }
        }

    }

}