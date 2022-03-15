package com.xeniac.dsfut.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.xeniac.dsfut.BaseApplication
import com.xeniac.dsfut.models.DsFutResponse
import com.xeniac.dsfut.repositories.MainRepository
import com.xeniac.dsfut.utils.Constants.ERROR_NETWORK_CONNECTION
import com.xeniac.dsfut.utils.Event
import com.xeniac.dsfut.utils.NetworkHelper.hasInternetConnection
import com.xeniac.dsfut.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(application) {

    private val _playerLiveData:
            MutableLiveData<Event<Resource<DsFutResponse>>> = MutableLiveData()
    val playerLiveData: LiveData<Event<Resource<DsFutResponse>>> = _playerLiveData

    fun pickUpPlayer(
        fifaVersion: String, platform: String, partnerId: String, currentTime: String,
        signature: String, minPrice: String?, maxPrice: String?, takeAfter: String?
    ) = viewModelScope.launch {
        safePickUpPlayer(
            fifaVersion, platform, partnerId, currentTime, signature, minPrice, maxPrice, takeAfter
        )
    }

    private suspend fun safePickUpPlayer(
        fifaVersion: String, platform: String, partnerId: String, currentTime: String,
        signature: String, minPrice: String?, maxPrice: String?, takeAfter: String?
    ) {
        _playerLiveData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection(getApplication<BaseApplication>())) {
                val responseBody = mainRepository.pickUpPlayer(
                    fifaVersion, platform, partnerId, currentTime, signature,
                    minPrice, maxPrice, takeAfter
                ).body()
                _playerLiveData.postValue(Event(Resource.Success(responseBody)))
                Timber.i("player picked up")
            } else {
                Timber.e(ERROR_NETWORK_CONNECTION)
                _playerLiveData.postValue(Event(Resource.Error(ERROR_NETWORK_CONNECTION)))
            }
        } catch (e: Exception) {
            Timber.e("safePickUpPlayer exception: ${e.message}")
            _playerLiveData.postValue(Event(Resource.Error(e.message.toString())))
        }
    }
}