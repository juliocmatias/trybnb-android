package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.widget.addTextChangedListener
import com.betrybe.trybnb.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class CreateReservationFragment : Fragment() {

    private lateinit var firstNameTextInputLayout: TextInputLayout
    private lateinit var lastNameTextInputLayout: TextInputLayout
    private lateinit var checkInTextInputLayout: TextInputLayout
    private lateinit var checkOutTextInputLayout: TextInputLayout
    private lateinit var additionalNeedsTextInputLayout: TextInputLayout
    private lateinit var totalPriceTextInputLayout: TextInputLayout
    private lateinit var depositPaidCheckbox: CheckBox
    private lateinit var btnCreateBookingMaterialButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstNameTextInputLayout = view.findViewById(R.id.first_name_create_reservation)
        lastNameTextInputLayout = view.findViewById(R.id.last_name_create_reservation)
        checkInTextInputLayout = view.findViewById(R.id.checkin_create_reservation)
        checkOutTextInputLayout = view.findViewById(R.id.checkout_create_reservation)
        additionalNeedsTextInputLayout = view.findViewById(R.id.additional_needs_create_reservation)
        totalPriceTextInputLayout = view.findViewById(R.id.total_price_create_reservation)
        depositPaidCheckbox = view.findViewById(R.id.depositpaid_create_reservation)
        btnCreateBookingMaterialButton = view.findViewById(R.id.create_reservation_button)
    }

    override fun onStart() {
        super.onStart()

        setupTextInputLayoutListener(firstNameTextInputLayout)
        setupTextInputLayoutListener(lastNameTextInputLayout)
        setupTextInputLayoutListener(checkInTextInputLayout)
        setupTextInputLayoutListener(checkOutTextInputLayout)
        setupTextInputLayoutListener(additionalNeedsTextInputLayout)
        setupTextInputLayoutListener(totalPriceTextInputLayout)

        btnCreateBookingMaterialButton.setOnClickListener {
            val firstName = firstNameTextInputLayout.editText?.text.toString()
            val lastName = lastNameTextInputLayout.editText?.text.toString()
            val checkInString = checkInTextInputLayout.editText?.text.toString()
            val checkOutString = checkOutTextInputLayout.editText?.text.toString()
            val additionalNeeds = additionalNeedsTextInputLayout.editText?.text.toString()
            val totalPrice = totalPriceTextInputLayout.editText?.text.toString()

            val isValidBooking = validateCreateBooking(
                firstName,
                lastName,
                checkInString,
                checkOutString,
                additionalNeeds,
                totalPrice)

            if (isValidBooking) {
                showSnack("Reserva feita com sucesso!")
            }
        }

    }

    private fun validateCreateBooking(
        firstName: String,
        lastName: String,
        checkInString: String,
        checkOutString: String,
        additionalNeeds: String,
        totalPrice: String
    ): Boolean {
        var isValid = true

        if (firstName.isEmpty()) {
            firstNameTextInputLayout.error = "O campo Nome é obrigatório"
            isValid = false
        } else {
            firstNameTextInputLayout.error = null
        }

        if (lastName.isEmpty()) {
            lastNameTextInputLayout.error = "O campo Sobrenome é obrigatório"
            isValid = false
        } else {
            lastNameTextInputLayout.error = null
        }

        if (checkInString.isEmpty()) {
            checkInTextInputLayout.error = "O campo Checkin é obrigatório"
            isValid = false
        } else {
            checkInTextInputLayout.error = null
        }

        if (checkOutString.isEmpty()) {
            checkOutTextInputLayout.error = "O campo Checkout é obrigatório"
            isValid = false
        } else {
            checkOutTextInputLayout.error = null
        }

        if (additionalNeeds.isEmpty()) {
            additionalNeedsTextInputLayout.error = "O campo Necessidades Adicionais é obrigatório"
            isValid = false
        } else {
            additionalNeedsTextInputLayout.error = null
        }

        if (totalPrice.isEmpty()) {
            totalPriceTextInputLayout.error = "O campo Preço Total é obrigatório"
            isValid = false
        } else {
            totalPriceTextInputLayout.error = null
        }

        return isValid
    }

    private fun showSnack(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupTextInputLayoutListener(textInputLayout: TextInputLayout) {
        textInputLayout.editText?.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                textInputLayout.error = null
            }
        }
    }

}