package com.betrybe.trybnb.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.repository.OpenBookingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

class ProfileFragmentViewModel : ViewModel() {

    sealed class UiState {
        data class Success(val message: String) : UiState()
        data class Error(val message: String) : UiState()
        data class Loading(val isLoading: Boolean) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    private val bookingServiceApi = OpenBookingService.instance

    fun login(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                _uiState.postValue(UiState.Loading(true))

                val token = getCurrencyAuth(login, password)

                withContext(Dispatchers.Main) {
                    if (token != null) {
                        _uiState.postValue(UiState.Success("Login feito com sucesso!"))
                    } else {
                        _uiState.postValue(UiState.Error("Usuário ou senha inválidos"))
                    }
                }
            } catch (e: UnknownHostException) {
                withContext(Dispatchers.Main) {
                    _uiState.postValue(UiState.Error("Erro: Sem conexão com a internet"))
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    _uiState.postValue(UiState.Error("Erro: Falha na comunicação com o servidor"))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.postValue(UiState.Error("Erro: Ocorreu um erro inesperado"))
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _uiState.postValue(UiState.Loading(false))
                }
                ApiIdlingResource.decrement()
            }
        }
    }

    private suspend fun getCurrencyAuth(login: String, password: String) : String? {
        val response = bookingServiceApi.getAuth(AuthRequest(login, password))

        return if (response.isSuccessful) {
            response.body()?.token
        } else {
            throw HttpException(response)
        }
    }


}