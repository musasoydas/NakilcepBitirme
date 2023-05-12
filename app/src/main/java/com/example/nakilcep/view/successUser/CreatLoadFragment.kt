package com.example.nakilcep.view.successUser

import android.app.DatePickerDialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nakilcep.R
import com.example.nakilcep.databinding.FragmentCreatLoadFragmentBinding
import com.example.nakilcep.extensions.showDialog
import com.example.nakilcep.extensions.showToast
import com.example.nakilcep.view.unsuccessUser.MainActivity
import com.example.nakilcep.viewmodel.CreateLoadViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CreatLoadFragment : Fragment() {

    private lateinit var binding: FragmentCreatLoadFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private val args: CreatLoadFragmentArgs by navArgs()
    private val viewModel: CreateLoadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatLoadFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        CreatdropDowns()

        viewModel.teslimatNoktasi.observe(viewLifecycleOwner) {
            binding.teslimatNoktasi.text = it
        }
        viewModel.yuklemeNoktasi.observe(viewLifecycleOwner) {
            binding.yuklemeNoktasi.text = it
        }

        binding.alisTarihi.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->

                    binding.alisTarihi.text = "" + mDay + "/" + (mMonth + 1) + "/" + mYear
                }, year, month, day
            )
            dpd.show()
        }
        binding.verisTarihi.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                    binding.verisTarihi.setText(" " + mDay + "/" + (mMonth + 1) + "/" + mYear)
                }, year, month, day
            )
            dpd.show()
        }
        binding.yuklemeNoktasi.setOnClickListener {
            requireContext().showDialog(
                R.drawable.question,
                "Adres Seçin",
                "Adres eklemek için kullandığınız türü seçiniz",
                "Yeni Adres",
                "Kayıtlı Adres ",
                onPositiveClick = {
                    val action = CreatLoadFragmentDirections.actionCreatLoadFragmentToNewAddress()
                    findNavController().navigate(action)
                },
                onNegativeClick = {
                    val action =
                        CreatLoadFragmentDirections.actionCreatLoadFragmentToRegisteredAddress("yukleme")
                    findNavController().navigate(action)
                }
            )
        }
        binding.teslimatNoktasi.setOnClickListener {
            requireContext().showDialog(
                R.drawable.question,
                "Adres Seçin",
                "Adres eklemek için kullandığınız türü seçiniz",
                "Yeni Adres",
                "Kayıtlı Adres ",
                onPositiveClick = {
                    val action = CreatLoadFragmentDirections.actionCreatLoadFragmentToNewAddress()
                    findNavController().navigate(action)
                },
                onNegativeClick = {
                    val action =
                        CreatLoadFragmentDirections.actionCreatLoadFragmentToRegisteredAddress("teslimat")
                    findNavController().navigate(action)
                }
            )
        }
        setFragmentResultListener("CreateLoad") { _, bundle ->
            bundle.getString("fromFragment")?.let {
                bundle.getString("address")?.let { address ->
                    if (it == "yukleme") {
                        viewModel.yuklemeNoktasi.value = address
                    } else {
                        viewModel.teslimatNoktasi.value = address
                    }
                }
            }
        }
        btnCreatLoadListener()
    }

    fun btnCreatLoadListener() {

        with(binding)
        {
            binding.btnLoadCreat.setOnClickListener {
                val postMap = hashMapOf(
                    "loadTakeDate" to alisTarihi.text.toString(),
                    "loadGiveDate" to verisTarihi.text.toString(),
                    "loadingPoint" to yuklemeNoktasi.text.toString(),
                    "deliveryPoint" to teslimatNoktasi.text.toString(),
                    "postUser" to auth.currentUser!!.email.toString(),
                    "loadTitle" to loadTitle.text.toString(),
                    "postDate" to Timestamp.now(),
                    "userId" to auth.currentUser!!.uid,
                    "postStatus" to false,
                    "loadGenus" to loadTypeDropdown.text.toString(),
                    "loadType" to loadTypeDropdown2.text.toString(),
                    "loadingType" to loadTypeDropdown3.text.toString(),
                    "loadExplanation" to loadExplanation.text.toString().trim(),
                    "documentId" to ""
                    )
                database.collection("Post").add(postMap).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //başarılı oldu
                        showNatification(loadTitle.text.toString())
                        val action=CreatLoadFragmentDirections.actionCreatLoadFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                }.addOnSuccessListener {documentReferences ->
                    val belgeId= documentReferences.id
                    documentReferences.update("documentId", belgeId)

                }.addOnFailureListener { exception ->
                    //ilan yüklenirken hata olursa eklenecekler
                    requireContext().showToast("HATA ALINDI")
                }
            }
        }
    }
    private fun CreatdropDowns(){
        val dropDownAdapterloadGenus = ArrayAdapter(requireContext(),R.layout.custom_view_dropdown,resources.getStringArray(R.array.loadGenus))
        binding.loadTypeDropdown.setAdapter(dropDownAdapterloadGenus)
        val dropDownAdapterLoadType = ArrayAdapter(requireContext(),R.layout.custom_view_dropdown,resources.getStringArray(R.array.loadType))
        binding.loadTypeDropdown2.setAdapter(dropDownAdapterLoadType)
        val dropDownAdapterLoadingType = ArrayAdapter(requireContext(),R.layout.custom_view_dropdown,resources.getStringArray(R.array.loadingType))
        binding.loadTypeDropdown3.setAdapter(dropDownAdapterLoadingType)
    }

    private fun showNatification(ilanAdi:String){
        val builder: NotificationCompat.Builder
        val notificationManager= requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent=Intent(requireContext(),MainActivity::class.java)
        val pendingIntent=PendingIntent.getActivity(requireContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val CHANNEL_ID = "kanal_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName="kanalAd"
            val kanalTanim="kanalTanım"
            val channelOnceligi=NotificationManager.IMPORTANCE_HIGH
            var channell : NotificationChannel?=notificationManager.getNotificationChannel(CHANNEL_ID)
            if(channell==null){
                channell=NotificationChannel(CHANNEL_ID,channelName,channelOnceligi)
                channell.description=kanalTanim
                notificationManager.createNotificationChannel(channell)
            }
            builder=NotificationCompat.Builder(requireContext(),CHANNEL_ID)
            builder.setSmallIcon(R.drawable.appicon)
                .setContentTitle("İlan Oluşturuldu")
                .setContentText("$ilanAdi ilanınız başrılı şekilde oluşturulmuştur. ")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)



        }else {
            builder = NotificationCompat.Builder(requireContext())
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle("İlan Oluşturuldu")
                .setContentText("$ilanAdi ilanınız başrılı şekilde oluşturulmuştur. ")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
//                .priority=Notification.PRIORITY_HIGH

        }
        notificationManager.notify(1, builder.build())
    }
}



