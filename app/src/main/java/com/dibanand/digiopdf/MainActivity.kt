package com.dibanand.digiopdf

import android.app.Notification.Action
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dibanand.digiopdf.databinding.ActivityMainBinding
import com.dibanand.digiopdf.repository.PdfRepository
import com.dibanand.digiopdf.ui.PdfViewModel
import com.dibanand.digiopdf.ui.PdfViewModelProviderFactory
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PdfViewModel
    private var name: String = "Dibyanshu"
    private var identifier: String = "dibyanshuanand2000@gmail.com"

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelProviderFactory = PdfViewModelProviderFactory(application, PdfRepository())
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PdfViewModel::class.java)

        setupUi()
        setupObservers()
    }

    private fun setupUi() {
        binding.btnUpload.setOnClickListener {
            name = binding.etName.text.toString()
            identifier = binding.etIdentifier.text.toString()
            if (!viewModel.validateUserInput(name, identifier)) {
                Toast.makeText(this, "Enter valid input combination", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "application/pdf"
            }
            startActivityForResult(intent, 1212)
        }

        binding.btnGetDocDetails.setOnClickListener {
            viewModel.getDocDetails()
        }

        binding.btnGetDoc.setOnClickListener {
            val filePath = "$cacheDir/out.pdf"
            viewModel.getPdf(filePath)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1212 && resultCode == RESULT_OK) {
            val pdfUri = data?.data
            if (pdfUri != null) {
                val stream = applicationContext.contentResolver.openInputStream(pdfUri)
                val file = File("$cacheDir/temp.pdf")
                val outStream = FileOutputStream(file)
                if (stream != null) {
                    FileUtils.copy(stream, outStream)
                    viewModel.uploadPdf(name, identifier, file)
                } else {
                    Log.e(TAG, "onActivityResult: Can't access file")
                    Toast.makeText(this, "Can't access file", Toast.LENGTH_SHORT).show()
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupObservers() {
        viewModel.documentRes.observe(this) { response ->
            binding.tvUploadResponse.text = response
        }

        viewModel.uploadStatus.observe(this) { status ->
            if (status) {
                binding.apply {
                    tvUploadResult.visibility = View.VISIBLE
                    tvUploadResponse.visibility = View.VISIBLE
                    btnGetDoc.visibility = View.VISIBLE
                    btnGetDocDetails.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    tvUploadResult.visibility = View.INVISIBLE
                    tvUploadResponse.visibility = View.INVISIBLE
                    btnGetDoc.visibility = View.INVISIBLE
                    btnGetDocDetails.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.docDetails.observe(this) {
            binding.tvDocDetails.visibility = View.VISIBLE
            binding.tvDocDetails.text = "agreement_status: $it"
        }

        viewModel.fileSaved.observe(this) { status ->
            if (status) {
                val filePath = "$cacheDir/out.pdf"
                val file = File(filePath)
                val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
                var intent = Intent(Intent.ACTION_VIEW).also {
                    it.data = uri
                    it.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                startActivity(intent)
            }
        }
    }
}