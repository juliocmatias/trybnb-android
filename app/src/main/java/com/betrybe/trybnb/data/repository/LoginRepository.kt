package com.betrybe.trybnb.data.repository

import com.betrybe.trybnb.data.datasource.network.LoginDataSource
import com.betrybe.trybnb.data.models.ServiceResponse
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class LoginRepository @Inject constructor(private val loginDataSource: LoginDataSource) {

    suspend fun getCurrencyAuth(login: String, password: String): ServiceResponse<String> {
        return try {
            val token = loginDataSource.getCurrencyAuth(login, password)

            if (token != null) {
                ServiceResponse.SuccessResponse(token, "Login feito com sucesso!")
            } else {
                ServiceResponse.ErrorResponse("Usuário ou senha inválidos")
            }
        } catch (e: UnknownHostException) {
            ServiceResponse.ErrorResponse("Erro: Sem conexão com a internet")
        } catch (e: HttpException) {
            ServiceResponse.ErrorResponse("Erro: Falha na comunicação com o servidor")
        } catch (e: Exception) {
            ServiceResponse.ErrorResponse("Erro: Ocorreu um erro inesperado")
        }
    }
}