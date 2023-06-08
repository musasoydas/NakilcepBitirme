package com.example.nakilcep.view.successUser

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Calendar
import java.util.UUID

class CreatLoadFragment : Fragment() {

    private lateinit var binding: FragmentCreatLoadFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private val args: CreatLoadFragmentArgs by navArgs()
    private val viewModel: CreateLoadViewModel by viewModels()
    var secilenGorsel: Uri? = null
    var secilenBitmap: Bitmap? = null
    val storage = Firebase.storage
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
        imageViewListner()

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
                val uuid = UUID.randomUUID()
                val calendar = Calendar.getInstance()

                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1 // Ay başlangıcı 0'dan başlar, bu yüzden 1 ekliyoruz.
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val reference = storage.reference
                val imageReference = reference.child("images").child("$uuid.jpg")
                imageReference.putFile(secilenGorsel!!).addOnSuccessListener {
                    val uploadedImageReference = reference.child("images").child("$uuid.jpg")
                    uploadedImageReference.downloadUrl.addOnSuccessListener {
                        var downloadUrl = it.toString()
                        val postMap = hashMapOf(
                            "detailDate" to ("$day"+"."+"$month"+"."+"$year").toString(),
                            "loadTakeDate" to alisTarihi.text.toString(),
                            "loadGiveDate" to verisTarihi.text.toString(),
                            "loadingPoint" to yuklemeNoktasi.text.toString(),
                            "deliveryPoint" to teslimatNoktasi.text.toString(),
                            "postUser" to auth.currentUser!!.email.toString(),
                            "loadTitle" to loadTitle.text.toString(),
                            "postDate" to Timestamp.now(),
                            "userId" to auth.currentUser!!.uid,
                            "postStatus" to "",
                            "loadGenus" to loadTypeDropdown.text.toString(),
                            "loadType" to loadTypeDropdown2.text.toString(),
                            "loadingType" to loadTypeDropdown3.text.toString(),
                            "loadExplanation" to loadExplanation.text.toString().trim(),
                            "documentId" to "",
                            "downloadUrl" to downloadUrl
                        )
                        database.collection("Post").add(postMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //başarılı oldu
                                showNatification(loadTitle.text.toString())
                                val action =
                                    CreatLoadFragmentDirections.actionCreatLoadFragmentToHomeFragment()
                                findNavController().navigate(action)
                            }
                        }.addOnSuccessListener { documentReferences ->
                            val belgeId = documentReferences.id
                            documentReferences.update("documentId", belgeId)

                        }.addOnFailureListener { exception ->
                            //ilan yüklenirken hata olursa eklenecekler
                            requireContext().showToast("HATA ALINDI")
                        }

                    }

                }.addOnFailureListener {
                    requireContext().showToast(it.localizedMessage)
                }

//
            }
        }
    }

    private fun CreatdropDowns() {
        val dropDownAdapterloadGenus = ArrayAdapter(
            requireContext(),
            R.layout.custom_view_dropdown,
            resources.getStringArray(R.array.loadGenus)
        )
        binding.loadTypeDropdown.setAdapter(dropDownAdapterloadGenus)
        val dropDownAdapterLoadType = ArrayAdapter(
            requireContext(),
            R.layout.custom_view_dropdown,
            resources.getStringArray(R.array.loadType)
        )
        binding.loadTypeDropdown2.setAdapter(dropDownAdapterLoadType)
        val dropDownAdapterLoadingType = ArrayAdapter(
            requireContext(),
            R.layout.custom_view_dropdown,
            resources.getStringArray(R.array.loadingType)
        )
        binding.loadTypeDropdown3.setAdapter(dropDownAdapterLoadingType)
    }

    private fun showNatification(ilanAdi: String) {
        val builder: NotificationCompat.Builder
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val CHANNEL_ID = "kanal_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "kanalAd"
            val kanalTanim = "kanalTanım"
            val channelOnceligi = NotificationManager.IMPORTANCE_HIGH
            var channell: NotificationChannel? =
                notificationManager.getNotificationChannel(CHANNEL_ID)
            if (channell == null) {
                channell = NotificationChannel(CHANNEL_ID, channelName, channelOnceligi)
                channell.description = kanalTanim
                notificationManager.createNotificationChannel(channell)
            }
            builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            builder.setSmallIcon(R.drawable.appicon)
                .setContentTitle("İlan Oluşturuldu")
                .setContentText("$ilanAdi ilanınız başrılı şekilde oluşturulmuştur. ")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)


        } else {
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

    fun imageViewListner() {
        binding.imageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            )// 32 izin verilmediyse hiç
            {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )//33 izni iste
            } else // 34 zaten izin verildiyse galeriye git
            {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }

        }

    }

    //35 yukarıda verdiğimiz requestkodları deneyeceğiz
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //36 izin verilince yapılacaklar
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //37 galeri işlemleri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null)//38 requestcode 2 ve galeriye gidip resmi seçtiyse ve seçtiği resim boş değilse
        {
            secilenGorsel = data.data//40 buraların hepsi klasik resim alma komutları
            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    var source =
                        ImageDecoder.createSource(requireContext().contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    binding.imageView.setImageBitmap(secilenBitmap)
                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        secilenGorsel
                    )
                    binding.imageView.setImageBitmap(secilenBitmap)

                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}



