package com.example.receipts

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.receipts.ui.BrowseReceiptsScreen
import com.example.receipts.ui.ConfirmScreen
import com.example.receipts.ui.HomeScreen
import com.example.receipts.ui.ReceiptsViewModel

enum class ReceiptsScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Confirmation(title = R.string.confirmation),
    BrowseReceipts(title = R.string.browse_receipts)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptsAppBar(
    currentScreen: ReceiptsScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun ReceiptsApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ReceiptsScreen.valueOf(
        backStackEntry?.destination?.route ?: ReceiptsScreen.Home.name
    )

    val viewModel: ReceiptsViewModel = viewModel()

    Scaffold(
        topBar = { ReceiptsAppBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() }
        ) }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ReceiptsScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ReceiptsScreen.Home.name) {
                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    viewModel = viewModel,
                    onTakePhoto = { bitmap ->
                        viewModel.updateCurrentReceipt(bitmap)
                        navController.navigate(ReceiptsScreen.Confirmation.name)
                    },
                    onBrowseReceipts = { navController.navigate(ReceiptsScreen.BrowseReceipts.name) },
                    uiState = uiState
                )
            }
            composable(route = ReceiptsScreen.Confirmation.name) {
                ConfirmScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onConfirm = {
                        viewModel.confirmCurrentReceipt()
                        navController.popBackStack()
                    },
                    onAmountChanged = { amount ->
                        viewModel.updateReceiptPrice(amount)
                    },
                    uiState = uiState,
                )
            }
            composable(route = ReceiptsScreen.BrowseReceipts.name) {
                BrowseReceiptsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    uiState = uiState
                )
            }
        }
    }
}