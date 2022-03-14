package com.xeniac.dsfut.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.xeniac.dsfut.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(application) {
}