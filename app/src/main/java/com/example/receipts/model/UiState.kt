package com.example.receipts.model

data class UiState (
    val receipts: List<Receipt> = emptyList(),
    val currentReceipt: Receipt? = null,
    val total: Double = 0.0
)