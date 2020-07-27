package com.dims.cardinfofinder.screens.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.dims.cardinfofinder.ocr.Processor
import com.dims.cardinfofinder.R
import com.dims.cardinfofinder.helpers.ViewModelFactory
import com.dims.cardinfofinder.ocr.OCRConfig
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class ScanFragment : Fragment() {
    private val requestID = 123

    private lateinit var surfaceView: SurfaceView
    private lateinit var detectedTextView: TextView
    private lateinit var proceedButton: Button
    private lateinit var cameraSource: CameraSource
    private lateinit var textRecognizer: TextRecognizer
    lateinit var viewModel: ScanViewModel
    private lateinit var ocrConfig: OCRConfig

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ocrConfig = OCRConfig(requireActivity().application)

        with(view){
            surfaceView = findViewById(R.id.surfaceView)
            proceedButton = findViewById(R.id.proceedButton)
            detectedTextView = findViewById(R.id.detectedTextView)
        }

        val factory = ViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ScanViewModel::class.java)

        textRecognizer = ocrConfig.textRecognizer
        if (!textRecognizer.isOperational) {
            Log.w("ScanFragment", "Detector dependencies are not yet available.")
            Snackbar.make(view,
                "OCR dependencies could not be downloaded", Snackbar.LENGTH_INDEFINITE)
                .setAction("CANCEL"){}
                .show()
        }else{
            cameraSource = ocrConfig.cameraSource
            initSurfaceView()
            val processor =
                Processor(viewModel::check)
            textRecognizer.setProcessor(processor)
        }

        viewModel.isVisible.observe(viewLifecycleOwner, Observer {
            when(it){
                false -> { proceedButton.isEnabled = false }
                true -> {
                    val detected = getString(R.string.detected_text) + viewModel.value.toString()
                    detectedTextView.text = detected
                    proceedButton.isEnabled = true 
                }
            }
        })

        proceedButton.setOnClickListener {
            val action =
                ScanFragmentDirections.actionScanFragmentToResultFragment(
                    viewModel.value!!
                )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun initSurfaceView() {
        if (
            ActivityCompat.checkSelfPermission(requireActivity().applicationContext,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), requestID)
        } else {
            surfaceView.holder
                .addCallback(
                    getSurfaceViewCallback(surfaceView, cameraSource)
                )
        }
    }

    private fun getSurfaceViewCallback(surfaceView: SurfaceView, cameraSource: CameraSource)
            : SurfaceHolder.Callback {
        return object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int,
                                        width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder?) {
                try {
                    cameraSource.start(surfaceView.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        if (requestCode != requestID) {
            Log.d("ScanFragment", "Got unexpected permission result: $requestCode")
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                cameraSource.start(surfaceView.holder)
            } catch (e: IOException) {
                Snackbar.make(requireView(),
                    "Unable to display camera feed", Snackbar.LENGTH_INDEFINITE)
                    .setAction("CANCEL"){}
                    .show()
                e.printStackTrace()
            }
        }
    }
}