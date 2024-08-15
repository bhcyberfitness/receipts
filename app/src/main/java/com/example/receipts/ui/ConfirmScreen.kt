package com.example.receipts.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.receipts.model.UiState
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun ConfirmScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAmountChanged: (Double) -> Unit = {},
    onConfirm: () -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var isValidAmount by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        uiState.currentReceipt?.image?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)
            )
        }
        Text(text = "Date: ${SimpleDateFormat("dd/MM/yyyy").format(Date())}")
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                val amount = it.text.toDoubleOrNull()
                isValidAmount = amount != null && amount >= 0.0
                if (isValidAmount) {
                    onAmountChanged(amount!!)
                }
            },
            label = { Text(text = "Enter Amount") },
            isError = !isValidAmount
        )

        if (!isValidAmount) {
            Text(
                text = "Invalid amount",
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = { onConfirm() },
            modifier = Modifier.widthIn(min = 250.dp),
            enabled = isValidAmount && text.text.isNotEmpty()
        ) {
            Text(text = "Confirm")
        }
    }
}