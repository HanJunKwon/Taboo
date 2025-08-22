package com.kwon.taboosample.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TextViewModel: ViewModel() {
    val userInput = MutableLiveData("")
    val error = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")
    val enabled = MutableStateFlow(false)

    fun onClick() {
        Log.d(">>>", "onClick() :: id: ${userInput.value}")
    }
}