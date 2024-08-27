package com.betrybe.trybnb.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.models.ServiceResponse
import com.betrybe.trybnb.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    sealed class UiState {
        data class Success(val message: String) : UiState()
        data class Error(val message: String) : UiState()
        data class Loading(val isLoading: Boolean) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState


    fun login(login: String, password: String) {
        _uiState.postValue(UiState.Loading(true))
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val responseToken = loginRepository.getCurrencyAuth(login, password)

                withContext(Dispatchers.Main) {
                    when (responseToken) {
                        is ServiceResponse.SuccessResponse -> {
                            _uiState.postValue(UiState.Success(responseToken.message))
                        }
                        is ServiceResponse.ErrorResponse -> {
                            _uiState.postValue(UiState.Error(responseToken.error))
                        }
                    }
                }
            }  catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.postValue(UiState.Error(e.message ?: "Erro: Ocorreu um erro inesperado"))
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _uiState.postValue(UiState.Loading(false))
                }
                ApiIdlingResource.decrement()
            }
        }
    }
}