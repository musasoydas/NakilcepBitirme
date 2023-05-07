package com.example.nakilcep.view.unsuccessUser

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.nakilcep.databinding.FragmentSignUpBinding
import com.example.nakilcep.extensions.showToast
import com.example.nakilcep.view.unsuccessUser.SignUpFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        // boş girilen yerlere kırmızı uyarı vermeye yarayan kod
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setStroke(2, Color.RED)


        with(binding)
        {

            signUpBtnUyeOl.setOnClickListener {

                if (radioYukVerici.isChecked || radiYukTasiyici.isChecked) {
                    radioGroup.background = null
                    if (signUpName.text.isNotEmpty()) {
                        signUpName.background = null
                        if (signUpLastname.text.isNotEmpty()) {
                            signUpLastname.background = null
                            if (signUpEposta.text.isNotEmpty()) {
                                signUpEposta.background = null
                                if (signUpPassword.text.isNotEmpty()) {
                                    signUpPassword.background = null
                                    var sifre = signUpPassword.text.toString()
                                    if (signUpAgainPassword.text.isNotEmpty()) {
                                        signUpAgainPassword.background = null
                                        if (sifre == signUpAgainPassword.text.toString()) {
                                            if (kosulChechbox.isChecked) {
                                                //En başarılı hali
                                                var email = signUpEposta.text.toString()
                                                var password = signUpAgainPassword.text.toString()
                                                var name = signUpName.text.toString()
                                                var surname = signUpLastname.text.toString()

                                                auth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            // kayıt olunmuştur
                                                            requireContext().showToast("Kayıt başarıyla oluşturulmuştur")
                                                            var action =
                                                                SignUpFragmentDirections.actionSignUpFragmentToLoginScreen(true)

                                                            findNavController().navigate(action
                                                            )
                                                            //val user = auth.currentUser

                                                            /*user!!.sendEmailVerification()
                                                                .addOnCompleteListener { task ->
                                                                    if (task.isSuccessful) {

                                                                    }

                                                                }*/


                                                        }
                                                    }.addOnFailureListener {
                                                        //hata alınca gözüksün
                                                        Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show();
                                                    }

                                            } else {
                                                requireContext().showToast("Kullanım Koşullarını Lütfen Onaylayınız")
                                                kosulChechbox.background = shape
                                            }
                                        } else {
                                            requireContext().showToast("Şifreleriniz Uyuşmamaktadır")
                                            signUpPassword.background = shape
                                            signUpAgainPassword.background = shape
                                        }
                                    } else {
                                        requireContext().showToast("Şifrenizi doldurunuz ")
                                        signUpPassword.background = shape
                                    }
                                } else {
                                    requireContext().showToast("Şifrenizi doldurunuz")
                                    signUpPassword.background = shape
                                }
                            } else {
                                requireContext().showToast("E-Posta adresinizi doldurunuz ")
                                signUpEposta.background = shape
                            }
                        } else {
                            requireContext().showToast("Soyisminizi doldurunuz")
                            signUpLastname.background = shape
                        }
                    } else {
                        requireContext().showToast("Lütfen İsminizi Doldurunuz ")
                        signUpName.background = shape
                    }
                } else {
                    requireContext().showToast("Lütfen Uygulamayı Nasıl Kullanacağınızı Seçiniz")
                    radioGroup.background = shape

                }
            }
            signUpTxtGiriseDon.setOnClickListener {
                val action = SignUpFragmentDirections.actionSignUpFragmentToLoginScreen()
                Navigation.findNavController(it).navigate(action)
            }
        }
    }



}