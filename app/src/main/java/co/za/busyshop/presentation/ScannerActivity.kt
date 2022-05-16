package co.za.busyshop.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import co.za.busyshop.R
import co.za.busyshop.util.ImageAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_scanner.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScannerActivity : AppCompatActivity() {
    companion object {
        private var TAG = "ScannerActivity"
    }

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var analyzer: ImageAnalyzer
    private lateinit var preview: PreviewView
    private lateinit var processCameraProvide: ProcessCameraProvider
   // private lateinit var myImageAnalyzer:MyImageAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

        }
        //preview = findViewById(R.id.myPreviewView)
        //window.setFlags(1024, 1024)

        /*cameraProviderFuture.addListener(Runnable {
            try {
                processCameraProvide = cameraProviderFuture.get()
                bindPreview(processCameraProvide)
            }catch (ex: ExecutionException){
                ex.printStackTrace()
            }catch (x: InterruptedException){
                x.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(this))*/

        analyzer = ImageAnalyzer(supportFragmentManager)
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            Log.d(TAG, "addListener:")
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        },ContextCompat.getMainExecutor(this))

    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {

        Log.d(TAG, "bindPreview: 111111111")
        val preview:Preview = Preview.Builder()
            .build()

        Log.d(TAG, "bindPreview: 2222222222222")

        val cameraSelector:CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

       /* val imageCapture = ImageCapture.Builder().build()
        myImageAnalyzer = MyImageAnalyzer(supportFragmentManager)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor, myImageAnalyzer)

        processCameraProvide?.unbindAll()
        processCameraProvide?.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis )*/

        Log.d(TAG, "bindPreview: 333333333333")
        preview.setSurfaceProvider(myPreviewView.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        Log.d(TAG, "bindPreview: 4444444444444")
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)
        Log.d(TAG, "bindPreview: 55555555555")
        cameraProvider?.bindToLifecycle(this as LifecycleOwner,
                      cameraSelector, imageAnalysis, preview)
        Log.d(TAG, "bindPreview: 666666666")

    }

    /*class MyImageAnalyzer(val fragmentManager: FragmentManager) : ImageAnalysis.Analyzer{

        override fun analyze(image: ImageProxy) {
            scanBarCode(image)
        }

        @SuppressLint("UnsafeOptInUsageError")
        private fun scanBarCode(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC)
                    .build()

                val scanner = BarcodeScanning.getClient(options)

                val result = scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        // Task completed successfully
                        readBarcodeData(barcodes)
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                    }
            }
        }

        private fun readBarcodeData(barcodes: List<Barcode>) {
            for (barcode in barcodes) {
                val bounds = barcode.boundingBox
                val corners = barcode.cornerPoints

                val rawValue = barcode.rawValue

                val valueType = barcode.valueType
                // See API reference for complete list of supported types
                when (valueType) {
                    Barcode.TYPE_WIFI -> {
                        val ssid = barcode.wifi!!.ssid
                        val password = barcode.wifi!!.password
                        val type = barcode.wifi!!.encryptionType
                    }
                    Barcode.TYPE_URL -> {
                        val title = barcode.url!!.title
                        val url = barcode.url!!.url
                    }
                }
            }
        }
    }*/
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){onBackPressed()}
        return super.onOptionsItemSelected(item)
    }




}