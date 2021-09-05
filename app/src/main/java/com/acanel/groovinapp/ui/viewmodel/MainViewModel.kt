package com.acanel.groovinapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.acanel.groovinapp.utils.L
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    init {
        L.d("MainViewModel", "init")
    }
}