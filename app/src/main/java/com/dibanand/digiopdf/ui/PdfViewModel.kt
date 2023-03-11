package com.dibanand.digiopdf.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dibanand.digiopdf.data.Signer
import com.dibanand.digiopdf.data.UploadPdf
import com.dibanand.digiopdf.repository.PdfRepository
import com.dibanand.digiopdf.util.Constants
import com.dibanand.digiopdf.util.FileUtils.convertFileToBase64
import com.dibanand.digiopdf.util.FileUtils.getFileNameFromPath
import com.dibanand.digiopdf.util.FileUtils.saveFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PdfViewModel(
    val application: Application,
    private val pdfRepository: PdfRepository
) : AndroidViewModel(application) {

    val documentRes: MutableLiveData<String> = MutableLiveData()
    val uploadStatus: MutableLiveData<Boolean> = MutableLiveData(false)
    val docDetails: MutableLiveData<String> = MutableLiveData()
    val fileSaved: MutableLiveData<Boolean> = MutableLiveData(false)

    companion object {
        const val TAG = "PdfViewModel"
    }

    fun uploadPdf(name: String, identifier: String, file: File) {
        uploadStatus.postValue(false)
        fileSaved.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileBody = UploadPdf(
                    mutableListOf(Signer(
                        identifier = identifier,
                        name = name,
                        reason = "Testing signing"
                    )),
                    10,
                    "all",
                    getFileNameFromPath(file.absolutePath),
                    // NOTE: Base64 encoding not working for some reason, that's why hardcoded
                    Constants.SAMPLE_FILE_BODY //convertFileToBase64(file)
                )
//                Log.e(TAG, "Base64 file string: ${convertFileToBase64(file)}")
                val response = pdfRepository.uploadPdf(fileBody)
                Log.e(TAG, "uploadPdf: Response - ${response.body()}")
                Toast.makeText(application, "uploadPdf: Response - ${response.body()}", Toast.LENGTH_SHORT).show()
                if (response.isSuccessful) {
                    documentRes.postValue(response.body()?.id)
                    uploadStatus.postValue(true)
                } else {
                    documentRes.postValue("Error in file upload")
                    uploadStatus.postValue(false)
                }
            } catch (e: Exception) {
                Log.e(TAG, "uploadPdf: Upload PDF failed - ${e.message}")
                Toast.makeText(application, "uploadPdf: Upload PDF failed - ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getDocDetails() {
        viewModelScope.launch {
            try {
                if (documentRes.value != null) {
                    val response = pdfRepository.getDocDetails(documentRes.value!!)
                    if (response.isSuccessful) {
                        docDetails.postValue(response.body()?.agreementStatus)
                    } else {
                        docDetails.postValue("Error")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Could not get details - ${e.message}")
                Toast.makeText(application, "Could not get details - ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getPdf(dirPath: String) {
        fileSaved.postValue(false)
        viewModelScope.launch {
            try {
                if (documentRes.value != null) {
                    val response = pdfRepository.getDocPdf(documentRes.value!!)
                    if (response.isSuccessful) {
                        saveFile(response.body(), dirPath)
                        fileSaved.postValue(true)
                    } else {
                        Log.e(TAG, "getPdf: Error in downloading PDF")
                        Toast.makeText(application, "getPdf: Error in downloading PDF", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Could not download PDF - ${e.message}")
                Toast.makeText(application, "Could not download PDF - ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun validateUserInput(name: String, identifier: String): Boolean {
        return name.isNotBlank() && identifier.isNotBlank() && name.length <= 100
    }
}