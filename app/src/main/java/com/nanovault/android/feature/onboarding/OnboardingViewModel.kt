package com.nanovault.android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanovault.android.engine.AICoreManager
import com.nanovault.android.engine.model.AICoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val aiCoreManager: AICoreManager
) : ViewModel() {

    val uiState: StateFlow<AICoreState> = aiCoreManager.state

    init {
        checkAICoreStatus()
    }

    fun checkAICoreStatus() {
        viewModelScope.launch {
            aiCoreManager.checkStatus()
        }
    }

    fun startDownload() {
        viewModelScope.launch {
            aiCoreManager.requestDownload()
        }
    }
}