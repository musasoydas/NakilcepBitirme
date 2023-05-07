package com.example.nakilcep.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateLoadViewModel: ViewModel() {

    val yuklemeNoktasi = MutableLiveData<String>()
    val teslimatNoktasi = MutableLiveData<String>()
}