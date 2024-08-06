package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.betrybe.trybnb.R
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.repository.OpenBookingService
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var loginInputProfile: TextInputLayout
    private lateinit var passwordInputProfile: TextInputLayout
    private lateinit var loginButtonProfile: Button
    private lateinit var waitingResponseState: FrameLayout

    private val bookingServiceApi = OpenBookingService.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginInputProfile = view.findViewById(R.id.login_input_profile)
        passwordInputProfile = view.findViewById(R.id.password_input_profile)
        loginButtonProfile = view.findViewById(R.id.login_button_profile)
        waitingResponseState = view.findViewById(R.id.waiting_response_state)
    }

    override fun onStart() {
        super.onStart()

        loginInputProfile.setEndIconOnClickListener {
            passwordInputProfile.requestFocus()
        }

        loginInputProfile.editText?.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                loginInputProfile.error = null
            }
        }

        passwordInputProfile.editText?.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                passwordInputProfile.error = null
            }
        }

        loginButtonProfile.setOnClickListener {
            val login = loginInputProfile.editText?.text.toString()
            val password = passwordInputProfile.editText?.text.toString()

            val isLoginValid = validateLoginProfile(login, password)

            if (isLoginValid) {

                showProgress()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        ApiIdlingResource.increment()

                        val bodyRequest = AuthRequest(login, password)

                        val response = bookingServiceApi.getAuth(bodyRequest)

                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                val token = response.body()?.token

                                if (token !== null) {
                                    hideProgress()
                                    showSnack("Login feito com sucesso!")
                                } else {
                                    hideProgress()
                                    showSnack("Usuário ou senha inválidos")
                                }
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                hideProgress()
                                showSnack("Erro ao efetuar login")
                            }
                        }

                        ApiIdlingResource.decrement()
                    } catch (e: IOException) {
                        ApiIdlingResource.decrement()
                        withContext(Dispatchers.Main) {
                            hideProgress()
                            showSnack("Error: Falha na comunicação com o servidor")
                        }
                    }
                }
            }
        }
    }

    private fun validateLoginProfile(login: String, password: String): Boolean {
        var isValid = true

        if (login.isEmpty()) {
            loginInputProfile.error = "O campo Login é obrigatório"
            isValid = false
        } else {
            loginInputProfile.error = null
        }

        if (password.isEmpty()) {
            passwordInputProfile.error = "O campo Password é obrigatório"
            isValid = false
        } else {
            passwordInputProfile.error = null
        }

        return isValid
    }

    private fun showProgress() {
        waitingResponseState.visibility = View.VISIBLE
        loginButtonProfile.visibility = View.GONE
    }

    private fun hideProgress() {
        waitingResponseState.visibility = View.GONE
        loginButtonProfile.visibility = View.VISIBLE
    }

    private fun showSnack(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

}