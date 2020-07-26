package com.dims.cardinfofinder.ocr

import android.app.Application
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer

class OCRConfig(application: Application){
    val textRecognizer: TextRecognizer = TextRecognizer.Builder(application).build()

    val cameraSource : CameraSource = CameraSource.Builder(application, textRecognizer)
        .setFacing(CameraSource.CAMERA_FACING_BACK)
        .setRequestedPreviewSize(1280, 1024)
        .setAutoFocusEnabled(true)
        .setRequestedFps(60.0f)
        .build()
}