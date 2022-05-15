package co.za.busyshop.util

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import co.za.busyshop.presentation.ShowDialog
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * Created by Bennette Molepo on 15/05/2022.
 */
class ImageAnalyzer(
    private val fragmentManager: FragmentManager
) : ImageAnalysis.Analyzer{

    private var showDialog = ShowDialog()

    override fun analyze(images: ImageProxy) {
        scanBarCode(images)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarCode(images: ImageProxy) {

        images.image?.let {
            image ->
            val inputImage = InputImage.fromMediaImage(
                image, images.imageInfo.rotationDegrees
            )
            val scanner = BarcodeScanning.getClient()
            scanner.process(inputImage)
                .addOnCompleteListener {
                    images.close()
                    if(it.isSuccessful){
                        readBarCode(it.result as List<Barcode>)
                    }else{
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

    private fun readBarCode(barcode: List<Barcode>) {

        for (barcodes in barcode){
            when(barcodes.valueType){
                Barcode.TYPE_URL ->{
                    if(!showDialog.isAdded){
                        showDialog.show(fragmentManager, "")
                    }
                }

            }
        }
    }

}