package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.models.AuthRequest
import com.betrybe.trybnb.data.repository.OpenBookingService
import com.betrybe.trybnb.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val bookingServiceApi = OpenBookingService.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextInputLayoutListener(binding.loginInputProfile)
        setupTextInputLayoutListener(binding.passwordInputProfile)

        binding.loginButtonProfile.setOnClickListener {
            val login = binding.loginInputProfile.editText?.text.toString()
            val password = binding.passwordInputProfile.editText?.text.toString()

            if (validateLoginProfile(login, password)) {

                showProgress()

                loginUser(login, password)
            }
        }
    }

    private fun loginUser(login: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val bodyRequest = AuthRequest(login, password)
                val response = bookingServiceApi.getAuth(bodyRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token

                        if (token !== null) {
                            hideProgress()
                            showSnack("Login feito com sucesso!")
                        } else {
                            hideProgress()
                            showSnack("Usuário ou senha inválidos")
                        }
                    } else {
                        hideProgress()
                        showSnack("Erro ao efetuar login")
                    }

                    ApiIdlingResource.decrement()
                }
            } catch (e: IOException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) {
                    hideProgress()
                    showSnack("Erro: Falha na comunicação com o servidor")
                }
            }
        }
    }


    private fun setupTextInputLayoutListener(textInputLayout: TextInputLayout) {
        textInputLayout.editText?.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                textInputLayout.error = null
            }
        }
        if (textInputLayout.hint == "Login") {
            textInputLayout.setEndIconOnClickListener {
                binding.passwordInputProfile.requestFocus()
            }
        }
    }

    private fun validateLoginProfile(login: String, password: String): Boolean {
        var isValid = true

        if (login.isEmpty()) {
            binding.loginInputProfile.error = "O campo Login é obrigatório"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordInputProfile.error = "O campo Password é obrigatório"
            isValid = false
        }

        return isValid
    }

    private fun showProgress() {
        binding.waitingResponseState.visibility = View.VISIBLE
        binding.loginButtonProfile.visibility = View.GONE
    }

    private fun hideProgress() {
        binding.waitingResponseState.visibility = View.GONE
        binding.loginButtonProfile.visibility = View.VISIBLE
    }

    private fun showSnack(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

}