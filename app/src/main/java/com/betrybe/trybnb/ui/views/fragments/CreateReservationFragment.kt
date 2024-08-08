package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.betrybe.trybnb.R
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.models.BookingData
import com.betrybe.trybnb.data.models.BookingDates
import com.betrybe.trybnb.data.repository.OpenBookingService
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CreateReservationFragment : Fragment() {

    private lateinit var firstNameTextInputLayout: TextInputLayout
    private lateinit var lastNameTextInputLayout: TextInputLayout
    private lateinit var checkInTextInputLayout: TextInputLayout
    private lateinit var checkOutTextInputLayout: TextInputLayout
    private lateinit var additionalNeedsTextInputLayout: TextInputLayout
    private lateinit var totalPriceTextInputLayout: TextInputLayout
    private lateinit var depositPaidCheckbox: CheckBox
    private lateinit var btnCreateBookingMaterialButton: MaterialButton
    private lateinit var waitingResponseStateCreateLayout: FrameLayout

    private val bookingServiceApi = OpenBookingService.instance

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
        waitingResponseStateCreateLayout = view.findViewById(R.id.waiting_response_state_create)
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
            val firstname = firstNameTextInputLayout.editText?.text.toString()
            val lastname = lastNameTextInputLayout.editText?.text.toString()
            val checkInString = checkInTextInputLayout.editText?.text.toString()
            val checkOutString = checkOutTextInputLayout.editText?.text.toString()
            val additionalneeds = additionalNeedsTextInputLayout.editText?.text.toString()
            val totalprice = totalPriceTextInputLayout.editText?.text.toString()
            val depositpaid = depositPaidCheckbox.isChecked

            val isValidBooking = validateCreateBooking(
                firstname,
                lastname,
                checkInString,
                checkOutString,
                additionalneeds,
                totalprice)

            if (isValidBooking) {

                showProgress()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        ApiIdlingResource.increment()

                        val bookingdates = BookingDates(
                            checkInString,
                            checkOutString
                        )

                        val bodyRequest = BookingData(
                            firstname,
                            lastname,
                            totalprice.toInt(),
                            depositpaid,
                            bookingdates,
                            additionalneeds
                        )

                        val response = bookingServiceApi.createBooking(bodyRequest)

                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                val status = response.message().toString()

                                if (status == "OK") {
                                    hideProgress()
                                    showSnack("Reserva feita com sucesso!")
                                } else {
                                    hideProgress()
                                    showSnack("Erro ao criar reserva")
                                }

                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                hideProgress()
                                showSnack("Erro ao criar reserva")
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

    private fun showProgress() {
        waitingResponseStateCreateLayout.visibility = View.VISIBLE
        btnCreateBookingMaterialButton.visibility = View.GONE
    }

    private fun hideProgress() {
        waitingResponseStateCreateLayout.visibility = View.GONE
        btnCreateBookingMaterialButton.visibility = View.VISIBLE
    }

    private fun setupTextInputLayoutListener(textInputLayout: TextInputLayout) {
        textInputLayout.editText?.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                textInputLayout.error = null
            }
        }
    }

}