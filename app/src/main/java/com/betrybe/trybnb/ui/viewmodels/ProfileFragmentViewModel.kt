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

class ProfileFragmentViewModel : ViewModel() {

    private val _error = MutableLiveData(false)
    val error: LiveData<Boolean>
        get() = _error

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean>
        get() = _success

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message

    private val _token = MutableLiveData("")
    val token: LiveData<String>
        get() = _token

    private val bookingServiceApi = OpenBookingService.instance

    fun login(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                _loading.postValue(true)

                val bodyRequest = AuthRequest(login, password)
                val response = bookingServiceApi.getAuth(bodyRequest)


                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token !== null) {
                        _loading.postValue(false)
                        _success.postValue(true)
                        _token.postValue(token)
                        _error.postValue(false)
                        _message.postValue("Login feito com sucesso!")
                    } else {
                        _success.postValue(false)
                        _loading.postValue(false)
                        _error.postValue(true)
                        _message.postValue("Usuário ou senha inválidos")
                    }
                } else {
                    _loading.postValue(false)
                    _error.postValue(true)
                    _message.postValue("Erro de conexão")
                }

                ApiIdlingResource.decrement()
            } catch (e: Exception) {
                ApiIdlingResource.decrement()
                _loading.postValue(false)
                _error.postValue(true)
                _message.postValue("Erro: Falha na comunicação com o servidor")
            }
        }
    }

}