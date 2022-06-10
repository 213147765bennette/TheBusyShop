package com.ikhokha.techcheck.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.fragment.app.FragmentManager
import com.google.firebase.database.*
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import com.ikhokha.techcheck.presentation.scan.ShowDialog
import javax.inject.Inject

/**
 * Created by Bennette Molepo on 15/05/2022.
 */
class ImageAnalyzer(
    private val context: Context,
    private val fragmentManager: FragmentManager
) : ImageAnalysis.Analyzer{
    companion object {
        private var TAG = "ImageAnalyzer"
    }

    var db: FirebaseDatabase? = null
    var dataNodeRef: DatabaseReference? = null
    @Inject
    lateinit var itemLocalRepository: ItemsDataRepository
    private lateinit var cartEntity: CartEntity
    private var quantity: Int = 0
    private var item: ShopItem? = null
    private var showDialog = ShowDialog()

    var fetchCount =0
   @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            // Define options for barcode scanner
            val options = BarcodeScannerOptions.Builder()
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnCompleteListener {
                    image.close()
                    if(it.isSuccessful){
                            for (barcode in it.result as List<Barcode>) {
                                Log.d(TAG,"111111111111")

                                //Read data from Firebase, update UI and save it to local db and pull it inside cart
                                readFirebaseData(barcode.rawValue)
                                // Handle received barcodes...
                                Toast.makeText(
                                    context,
                                    "Value: " + barcode.rawValue,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                    }else{
                        Log.d(TAG,"EROORRRRRRRRRRRRRRR")
                        it.exception?.printStackTrace()
                    }
                }
                .addOnFailureListener { }
        }

        image.close()
    }

    private fun readFirebaseData(scannedCode: String?) {

        item = ShopItem("","" ,0.0 )
        db = FirebaseDatabase.getInstance()
        dataNodeRef = db!!.getReference(scannedCode!!)
        dataNodeRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){
                    item = dataSnapshot.getValue(ShopItem::class.java)
                    Log.d(TAG,"Firebase ShopItem 11: $item")

                    fetchCount++
                    Log.d(TAG,"COUNT: $fetchCount")
                    if (fetchCount ==1){

                        showDialog.show(fragmentManager, "")
                        item.let {
                            if (it != null) {
                                Log.d(TAG,"LETS UPDATE THE UI")
                                //showDialog.updateDialogUI(it)
                            }
                        }
                    }else
                    {
                        Log.d(TAG,"DONT SHOW DIALOG")

                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                //showDialog.dismiss()
            }
        })

    }


}