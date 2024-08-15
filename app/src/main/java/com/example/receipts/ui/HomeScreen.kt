package com.example.receipts.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.receipts.model.UiState

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: ReceiptsViewModel,
    onTakePhoto: (Bitmap) -> Unit = {},
    onBrowseReceipts: () -> Unit = {},
    uiState: UiState
) {
    val context = LocalContext.current
    val cameraPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        cameraPermissionGranted.value = isGranted
    }
    val imageCaptureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        bitmap?.let {
            onTakePhoto(it)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
       Column (
            horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Text(text = "Running Total:", fontSize = 22.sp, fontWeight = FontWeight.Bold)
           Text(text = String.format("£%.2f", uiState.total))
       }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            if(cameraPermissionGranted.value) {
                Button(
                    onClick = { imageCaptureLauncher.launch() },
                    modifier = Modifier.widthIn(min = 250.dp)
                ) {
                    Text(text = "Take Photo")
                }
            } else {
                Button(
                    onClick = { launcher.launch(Manifest.permission.CAMERA) },
                    modifier = Modifier.widthIn(min = 250.dp)
                ) {
                    Text(text = "Grant Camera Permission")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onBrowseReceipts() },
                modifier = Modifier.widthIn(min = 250.dp)
            ) {
                Text(text = "Browse Receipts")
            }
        }
    }
}

