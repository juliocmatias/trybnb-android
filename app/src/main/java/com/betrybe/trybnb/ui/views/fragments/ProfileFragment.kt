package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.betrybe.trybnb.R
import com.google.android.material.textfield.TextInputLayout

class ProfileFragment : Fragment() {

    private lateinit var loginInputProfile: TextInputLayout
    private lateinit var passwordInputProfile: TextInputLayout
    private lateinit var loginButtonProfile: Button

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
    }

    override fun onStart() {
        super.onStart()

        loginInputProfile.setEndIconOnClickListener {
            passwordInputProfile.requestFocus()
        }

        loginButtonProfile.setOnClickListener {
            val login = loginInputProfile.editText?.text.toString()
            val password = passwordInputProfile.editText?.text.toString()

            if (login.isEmpty()) {
                loginInputProfile.error = "O campo Login é obrigatório"
            } else {
                loginInputProfile.error = null
            }

            if (password.isEmpty()) {
                passwordInputProfile.error = "O campo Password é obrigatório"
            } else {
                passwordInputProfile.error = null
            }
        }
    }

}