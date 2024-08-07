package com.betrybe.trybnb.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.betrybe.trybnb.data.models.BookingData
import com.betrybe.trybnb.R
import java.util.Locale

class ReservationAdapter(
    private val bookings: List<BookingData>
) : Adapter<ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val depositpaidImageInfo: ImageView = itemView
            .findViewById(R.id.depositpaid_item_reservation)
        private val guestNameTextView: TextView = itemView
            .findViewById(R.id.name_item_reservation)
        private val checkinTextView: TextView = itemView
            .findViewById(R.id.checkin_item_reservation)
        private val checkoutTextView: TextView = itemView
            .findViewById(R.id.checkout_item_reservation)
        private val additionalNeedsTextView: TextView = itemView
            .findViewById(R.id.additional_needs_item_reservation)
        private val totalpriceTextView: TextView = itemView
            .findViewById(R.id.total_price_item_reservation)

        fun bind(booking: BookingData) {
            val depositPaidImage = if (booking.depositpaid) {
                R.drawable.ic_depositpaid_true
            } else {
                R.drawable.ic_depositpaid_false
            }
            depositpaidImageInfo.setImageResource(depositPaidImage)
            "${booking.firstname} ${booking.lastname}".also { guestNameTextView.text = it }
            checkinTextView.text = booking.bookingdates.checkIn
            checkoutTextView.text = booking.bookingdates.checkOut
            additionalNeedsTextView.text = booking.additionalneeds
            val totalPrice = String.format(Locale.getDefault(), "%.2f", booking.totalprice)
            totalpriceTextView.text = totalPrice
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ReservationViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun getItemCount(): Int = bookings.size

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(bookings[position])
    }
}