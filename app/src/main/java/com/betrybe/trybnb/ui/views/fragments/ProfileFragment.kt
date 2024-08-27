package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.betrybe.trybnb.databinding.FragmentProfileBinding
import com.betrybe.trybnb.ui.viewmodels.ProfileFragmentViewModel
import com.betrybe.trybnb.ui.views.activities.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ProfileFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextInputLayoutListener(binding.loginInputProfile)
        setupTextInputLayoutListener(binding.passwordInputProfile)

        binding.loginButtonProfile.setOnClickListener {
            val login = binding.loginInputProfile.editText?.text.toString()
            val password = binding.passwordInputProfile.editText?.text.toString()

            if (validateLoginProfile(login, password)) {
                viewModel.login(login, password)
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ProfileFragmentViewModel.UiState.Success -> showSnack(uiState.message)
                is ProfileFragmentViewModel.UiState.Loading -> progressState(uiState.isLoading)
                is ProfileFragmentViewModel.UiState.Error -> showError(uiState.message)
            }
        }
    }

    private fun setupTextInputLayoutListener(textInputLayout: TextInputLayout) {
        loginInputProfileFocus()

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

    private fun clearFields() {
        binding.loginInputProfile.editText?.text?.clear()
        binding.passwordInputProfile.editText?.text?.clear()
    }

    private fun loginInputProfileFocus() {
        binding.loginInputProfile.requestFocus()
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

    private fun showError(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
        clearFields()
        loginInputProfileFocus()
    }

    private fun progressState(isLoading: Boolean) {
        if (isLoading) {
            showProgress()
        } else {
            hideProgress()
        }
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