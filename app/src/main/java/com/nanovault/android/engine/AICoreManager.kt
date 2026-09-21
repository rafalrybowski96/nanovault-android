package com.nanovault.android.engine

import com.nanovault.android.engine.model.AICoreState
import kotlinx.coroutines.flow.StateFlow

interface AICoreManager {
    val state: StateFlow<AICoreState>
    suspend fun checkStatus()
    suspend fun requestDownload()
    suspend fun initializeModel(): Boolean
}