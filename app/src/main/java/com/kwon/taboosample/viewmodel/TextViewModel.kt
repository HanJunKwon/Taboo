package com.kwon.taboosample.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TextViewModel: ViewModel() {
    val userInput = MutableLiveData<String>("")

    fun onClick() {
        Log.d(">>>", "onClick() :: id: ${userInput.value}")
    }
}