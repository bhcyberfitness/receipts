package com.example.receipts.ui

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.receipts.model.Receipt
import com.example.receipts.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

class ReceiptsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateCurrentReceipt(image: Bitmap) {
        val newReceipt = Receipt(
            id = _uiState.value.receipts.size + 1,
            price = 0.00,
            date = Date(),
            image = image
        )
        _uiState.value = _uiState.value.copy(
            currentReceipt = newReceipt
        )
    }

    fun updateReceiptPrice(price: Double) {
        val updatedReceipt = _uiState.value.currentReceipt?.copy(price = price)
        _uiState.value = _uiState.value.copy(
            currentReceipt = updatedReceipt
        )
    }

    fun confirmCurrentReceipt() {
        _uiState.value.currentReceipt?.let {
            _uiState.value = _uiState.value.copy(
                receipts = _uiState.value.receipts + it,
                total = _uiState.value.total + it.price,
                currentReceipt = null
            )

        }
    }
}