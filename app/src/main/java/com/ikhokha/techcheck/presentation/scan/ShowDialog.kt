package com.ikhokha.techcheck.presentation.scan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ikhokha.techcheck.MainActivity
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.databinding.ShowItemDialogBinding
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Bennette Molepo on 25/05/2022.
 */
@AndroidEntryPoint
class ShowDialog : DialogFragment() {

    companion object {
        private var TAG = "ShowDialog"
    }

    @Inject
    lateinit var itemLocalRepository: ItemsDataRepository
    private lateinit var cartEntity: CartEntity
    private var item: ShopItem? = null
    private lateinit var txtItemDesc: TextView
    private lateinit var txtItemPrice:TextView
    private lateinit var itemImg:ImageView
    private lateinit var addButton: AppCompatButton
    private lateinit var noButton: AppCompatButton
    private lateinit var _binding: ShowItemDialogBinding
    private var isDialogLoaded:Boolean = false
    lateinit var myView:View

    private val itemImageList = listOf(
        "apple.jpg","banana.jpg","coconut.jpg","grapefruit.jpg",
        "orange.jpg","pear.jpg","strawberry.jpg","watermelon.jpg"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.show_item_dialog, container, false)
        init(myView)
        return  myView
    }

    @SuppressLint("SetTextI18n")
    private fun init(view: View) {
        addButton = view.findViewById(R.id.yesBtn)
        noButton = view.findViewById(R.id.noBtn)

        txtItemDesc = view.findViewById(R.id.txt_item_description)
        txtItemPrice = view.findViewById(R.id.txt_item_price)
        itemImg = view.findViewById(R.id.item_image)

        addButton.setOnClickListener {
            //when add btn pressed, store item to the db.
            storeItemToCart(item)

        }

        noButton.setOnClickListener {

            dialog?.dismiss()
        }

        if (isDialogLoaded){
            item = getShopItems()
            Log.d(TAG,"Dialog ShopItem Under INIT: $item")
            txtItemDesc.text= item?.description
            txtItemPrice.text = "R"+item?.price.toString()



            val storage = FirebaseStorage.getInstance()

            //val storageRef = storage.reference
            val gsReference = storage.getReferenceFromUrl("gs://the-busy-shop.appspot.com/banana.jpg")

            //code to load image
            Glide.with(requireContext()).
            load(gsReference).
            into(itemImg)
        }

    }

    private fun setItemImageUrl(itemCode:String){

    }


    /*private fun readItemImage(){
        //val storageReference = Firebase.storage.reference
        val storage = FirebaseStorage.getInstance()
        val storageReference = Firebase.storage.reference
        //val storageRef = storage.reference
        val gsReference = storage.getReferenceFromUrl("gs://the-busy-shop.appspot.com/banana.jpg")

        gsReference.child()
        //code to load image
        Glide.with(requireContext()).
        load(gsReference).
        into(itemImg)
    }*/

    @SuppressLint("SetTextI18n")
    fun updateDialogUI(shopItem: ShopItem){
        item = shopItem
        isDialogLoaded = true
        Log.d(TAG,"Dialog ShopItem: $item")
    }

    private fun getShopItems(): ShopItem? {
        isDialogLoaded = true
        return item
    }


    private fun storeItemToCart(item: ShopItem?) {

        //before store check if the itemcode does exist on the db,
        // if yes then call the dbd data by that itemcode,then update the quantity

        cartEntity = CartEntity(0L,item?.itemCode.toString(), item?.description.toString(), item!!.image.toString(),
            item.price, 1)

        lifecycleScope.launch {
            val cartList:List<CartEntity> = itemLocalRepository.getCartItems()
            if(cartList.isNotEmpty()){
                cartList.forEach { response ->
                    if (response.itemCode == item.itemCode){
                        Log.d(TAG,"Same Item:11111111")
                        response.quantity++
                        response.price = response.price * response.quantity
                        itemLocalRepository.updateItem(response)
                        Toast.makeText(context,"Updated item quantity.", Toast.LENGTH_LONG).show()
                    }else{
                        Log.d(TAG,"New Item:2222222222222")
                        itemLocalRepository.insertCartItem(cartEntity)
                        Toast.makeText(context,"Item added to a cart.", Toast.LENGTH_LONG).show()
                    }

                }
            }else{
                Log.d(TAG,"========LIST IS EMPTY, JUST ADD")
                itemLocalRepository.insertCartItem(cartEntity)
                Toast.makeText(context,"Item added to a cart.", Toast.LENGTH_LONG).show()
            }


        }

        startActivity(Intent(context,MainActivity::class.java))

    }


}