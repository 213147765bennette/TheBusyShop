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
import com.bumptech.glide.Glide
import com.ikhokha.techcheck.MainActivity
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.databinding.ShowItemDialogBinding
import com.ikhokha.techcheck.domain.repository.ItemsDataRepository
import dagger.hilt.android.AndroidEntryPoint
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
    private var quantity: Int = 0
    private var item: ShopItem? = null
    private lateinit var txtItemDesc: TextView
    private lateinit var txtItemPrice:TextView
    private lateinit var itemImg:ImageView
    private lateinit var addButton: AppCompatButton
    private lateinit var noButton: AppCompatButton
    private lateinit var _binding: ShowItemDialogBinding
    private var isDialogLoaded:Boolean = false

    lateinit var myView:View

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
        Log.d(TAG,"BINDING 333333333333")
        addButton = view.findViewById(R.id.yesBtn)
        noButton = view.findViewById(R.id.noBtn)

        txtItemDesc = view.findViewById(R.id.txt_item_description)
        txtItemPrice = view.findViewById(R.id.txt_item_price)
        itemImg = view.findViewById(R.id.item_image)

        addButton.setOnClickListener {
            //when add btn pressed, store item to the db.
            storeItemToCart(item)
            startActivity(Intent(context,MainActivity::class.java))
        }

        noButton.setOnClickListener {
            //close dialog
        }

        if (isDialogLoaded){
            item = getShopItems()
            Log.d(TAG,"Dialog ShopItem Under INIT: $item")
            txtItemDesc.text= item?.description
            txtItemPrice.text = "R"+item?.price.toString()

            //code to load image
            Glide.with(itemImg.context).
            load("https://images.app.goo.gl/Lebtgxmsg1SFq5N49").
            into(itemImg)
        }

    }


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

        Log.d(TAG,"Update shop cart: ${cartEntity.price}  ${cartEntity.description}")

        itemLocalRepository.insertCartItem(cartEntity)
        Toast.makeText(context,"Item added to a cart.", Toast.LENGTH_LONG).show()

    }


}