package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.trybnb.R
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.models.BookingData
import com.betrybe.trybnb.data.repository.OpenBookingService
import com.betrybe.trybnb.ui.adapters.ReservationAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ReservationFragment : Fragment() {

    private lateinit var reservationRecyclerView: RecyclerView
    private lateinit var waitingResponseReservation : FrameLayout

    private val bookingServiceApi = OpenBookingService.instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reservationRecyclerView = view.findViewById(R.id.reservation_recycler_view)
        waitingResponseReservation = view.findViewById(R.id.waiting_response_frame_layout)
    }

    override fun onStart() {
        super.onStart()

        showProgress()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()

                val idsResponse = bookingServiceApi.getBookingIds()

                if (idsResponse.isSuccessful) {
                    val bookingIds = idsResponse.body() ?: emptyList()
                    val bookings: MutableList<BookingData> = mutableListOf()

                    val deferredBookings = bookingIds.take(4).map { id ->
                        async {
                            val bookingResponse = bookingServiceApi.getBookingById(id.bookingid.toString())
                            if (bookingResponse.isSuccessful) {
                                bookingResponse.body()
                            } else {
                                null
                            }
                        }
                    }

                    val bookingResults = deferredBookings.awaitAll().filterNotNull()
                    bookings.addAll(bookingResults)

                    withContext(Dispatchers.Main) {
                        Log.d("BOOKING", bookings.toString())
                        val reservationAdapter = ReservationAdapter(bookings)
                        reservationRecyclerView
                            .layoutManager = LinearLayoutManager(context)
                        reservationRecyclerView.adapter = reservationAdapter

                        hideProgress()
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        hideProgress()
                        showSnack()
                    }
                }
                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) {
                    hideProgress()
                    showSnack()
                }

            } catch (e: IOException) {
                ApiIdlingResource.decrement()
                withContext(Dispatchers.Main) {
                    hideProgress()
                    showSnack()
                }
            }
        }
    }

    private fun showProgress() {
        waitingResponseReservation.visibility = View.VISIBLE
        reservationRecyclerView.visibility = View.GONE
    }

    private fun hideProgress() {
        waitingResponseReservation.visibility = View.GONE
        reservationRecyclerView.visibility = View.VISIBLE
    }

    private fun showSnack() {
        val message = "Erro ao buscar reservas"
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}