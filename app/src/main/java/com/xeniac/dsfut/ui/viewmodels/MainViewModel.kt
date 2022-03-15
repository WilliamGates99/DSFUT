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
        feedUrl: String,
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfter: String? = null
    ) = viewModelScope.launch {
        safePickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
    }

    private suspend fun safePickUpPlayer(
        feedUrl: String,
        minPrice: String? = null,
        maxPrice: String? = null,
        takeAfter: String? = null
    ) {
        _playerLiveData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection(getApplication<BaseApplication>())) {
                val response = mainRepository.pickUpPlayer(feedUrl, minPrice, maxPrice, takeAfter)
                response.body()?.let {
                    when {
                        it.error.isNotBlank() -> {
                            val errorMessage = "${it.error} error: ${it.message}"
                            _playerLiveData.postValue(Event(Resource.Error(errorMessage)))
                        }
                        else -> _playerLiveData.postValue(Event(Resource.Success(it)))
                    }
                    Timber.i("safePickUpPlayer: ${it.message}")
                }
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