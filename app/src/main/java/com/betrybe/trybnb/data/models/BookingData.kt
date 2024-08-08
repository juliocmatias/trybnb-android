package com.betrybe.trybnb.data.models

data class BookingData(
    val firstname: String,
    val lastname: String,
    val totalprice: Int,
    val depositpaid: Boolean,
    val bookingdates: BookingDates,
    val additionalneeds: String,
    val bookingid: String? = null,
)
