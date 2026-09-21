package com.nanovault.android.feature.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nanovault.android.engine.model.AICoreState

@Composable
fun OnboardingStatusRoute(
    onReady: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    OnboardingStatusScreen(
        state = state,
        onRetry = viewModel::checkAICoreStatus,
        onDownload = viewModel::startDownload,
        onContinue = onReady
    )
}

@Composable
fun OnboardingStatusScreen(
    state: AICoreState,
    onRetry: () -> Unit,
    onDownload: () -> Unit,
    onContinue: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "NanoVault AI Core",
                    style = MaterialTheme.typography.headlineMedium
                )

                when (state) {
                    is AICoreState.Idle, AICoreState.Checking -> {
                        CircularProgressIndicator()
                        Text("Sprawdzanie dostępności Gemini Nano na NPU...")
                    }
                    is AICoreState.Ready -> {
                        Text(
                            text = "Model Gemini Nano jest aktywny i gotowy do pracy lokalnej.",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Button(onClick = onContinue) {
                            Text("Przejdź do aplikacji")
                        }
                    }
                    is AICoreState.NeedsDownload -> {
                        Text("Wymagane jest pobranie wag modelu przez usługę AICore.")
                        Button(onClick = onDownload) {
                            Text("Rozpocznij pobieranie")
                        }
                    }
                    is AICoreState.Downloading -> {
                        CircularProgressIndicator()
                        Text("Trwa pobieranie wag Gemini Nano przez system...")
                    }
                    is AICoreState.Unsupported -> {
                        Text(
                            text = "Urządzenie nie wspiera systemowego AICore dla tego modelu.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    is AICoreState.Error -> {
                        Text(
                            text = "Błąd: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = onRetry) {
                            Text("Spróbuj ponownie")
                        }
                    }
                }
            }
        }
    }
}