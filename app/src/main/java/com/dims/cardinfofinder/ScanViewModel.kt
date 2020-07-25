package com.dims.cardinfofinder

import android.annotation.SuppressLint
import android.app.Application
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException

class ScanViewModel(application: Application) : AndroidViewModel(application) {
    val textRecognizer: TextRecognizer = TextRecognizer.Builder(application).build()
    var value: Int? = null
    private val _isVisible = MutableLiveData(false)
    val isVisible: LiveData<Boolean> get() = _isVisible

    fun buildCameraSource(): CameraSource {
        return CameraSource.Builder(getApplication(), textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1280, 1024)
            .setAutoFocusEnabled(true)
            .setRequestedFps(60.0f)
            .build()
    }

    private fun check(text: String){
        if (text.length == 8) {
            try {
                value = Integer.valueOf(text)
                _isVisible.postValue(true)
            } catch (e: NumberFormatException) {
                _isVisible.postValue((value != null))
            }
        }
    }

    fun getSurfaceViewCallback(surfaceView: SurfaceView, cameraSource: CameraSource, requestID: Int): SurfaceHolder.Callback {
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

    fun getOCRDetector(): Detector.Processor<TextBlock>? {
        return object : Detector.Processor<TextBlock> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<TextBlock>) {
                val items = detections.detectedItems
                if (items.size() != 0) {
                    for (i in 0 until items.size()) {
                        val item = items.valueAt(i)
                        item.components.forEach {
                            Log.e("TEXT", it.value)
                            check(it.value.trim()
                                .replace(" ", "").take(8))
                        }
                    }
                }
            }
        }
    }
}