package com.nanovault.android.engine

import android.content.Context
import com.google.mlkit.genai.common.DownloadStatus
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel
import com.nanovault.android.engine.model.AICoreState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AICoreManagerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AICoreManager {

    private val _state = MutableStateFlow<AICoreState>(AICoreState.Idle)
    override val state: StateFlow<AICoreState> = _state.asStateFlow()

    private var generativeModel: GenerativeModel? = null

    override suspend fun checkStatus() {
        _state.value = AICoreState.Checking
        try {
            val model = Generation.getClient()
            when (val status = model.checkStatus()) {
                FeatureStatus.AVAILABLE -> {
                    generativeModel = model
                    _state.value = AICoreState.Ready
                }
                FeatureStatus.DOWNLOADABLE -> {
                    _state.value = AICoreState.NeedsDownload()
                }
                FeatureStatus.DOWNLOADING -> {
                    _state.value = AICoreState.Downloading
                }
                FeatureStatus.UNAVAILABLE -> {
                    _state.value = AICoreState.Unsupported
                }
                else -> {
                    _state.value = AICoreState.Error("Unknown status: $status")
                }
            }
        } catch (e: Exception) {
            _state.value = AICoreState.Error(e.message ?: "Failed to query AICore")
        }
    }

    override suspend fun requestDownload() {
        _state.value = AICoreState.Downloading
        try {
            val model = Generation.getClient()
            model.download().collect {
                when (it) {
                    DownloadStatus.DownloadCompleted -> {
                        _state.value = AICoreState.Ready
                        generativeModel = model
                    }
                    is DownloadStatus.DownloadFailed -> {
                        _state.value = AICoreState.Error(it.e.message ?: "Download failed")
                    }
                    is DownloadStatus.DownloadStarted,
                    is DownloadStatus.DownloadProgress -> {
                        _state.value = AICoreState.Downloading
                    }
                }
            }

        } catch (e: Exception) {
            _state.value = AICoreState.Error(e.message ?: "Download request error")
        }
    }

    override suspend fun initializeModel(): Boolean {
        if (generativeModel == null) {
            checkStatus()
        }
        return _state.value is AICoreState.Ready
    }
}