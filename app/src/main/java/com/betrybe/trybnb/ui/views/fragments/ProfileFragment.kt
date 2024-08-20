package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.betrybe.trybnb.databinding.FragmentProfileBinding
import com.betrybe.trybnb.ui.viewmodels.ProfileFragmentViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

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
        viewModel = ViewModelProvider(this)[ProfileFragmentViewModel::class.java]

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
                viewModel.login(login, password)
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            if (viewModel.success.value == true) {
                showSnack(message)
            }
            if (viewModel.error.value == true) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .setCancelable(false)
                    .show()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showProgress()
            } else {
                hideProgress()
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