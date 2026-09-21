package com.nanovault.android.engine.model

sealed interface AICoreState {
    data object Idle : AICoreState
    data object Checking : AICoreState
    data class NeedsDownload(val downloadProgress: Float = 0f) : AICoreState
    data object Downloading : AICoreState
    data object Ready : AICoreState
    data object Unsupported : AICoreState
    data class Error(val message: String) : AICoreState
}