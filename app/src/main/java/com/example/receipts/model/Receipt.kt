package com.example.receipts.model

import android.graphics.Bitmap
import java.util.Date

data class Receipt (
    val id: Int,
    val price: Double,
    val date: Date,
    val image: Bitmap? = null
)