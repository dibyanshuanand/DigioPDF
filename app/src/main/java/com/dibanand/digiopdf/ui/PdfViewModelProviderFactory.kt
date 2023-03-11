package com.dibanand.digiopdf.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dibanand.digiopdf.repository.PdfRepository

class PdfViewModelProviderFactory(
    val app: Application,
    val repository: PdfRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PdfViewModel(app, repository) as T
    }
}