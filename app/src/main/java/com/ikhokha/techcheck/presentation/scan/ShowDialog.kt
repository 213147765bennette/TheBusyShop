package com.ikhokha.techcheck.presentation.scan

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
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

    lateinit var myView:View

    @SuppressLint("InflateParams")
    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater

        Log.d(TAG,"AlertDialog Create")

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val customView: View = inflater.inflate(R.layout.show_item_dialog, null)
        txtItemDesc = customView.findViewById<View>(R.id.txt_item_description) as TextView
        txtItemPrice = customView.findViewById<View>(R.id.txt_item_price) as TextView

        return builder.create()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.show_item_dialog, container, false)
        return  myView
    }



     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        init(view)
        Log.d(TAG,"BINDING 22222222222")
    }


    private fun init(view: View) {

        Log.d(TAG,"BINDING 333333333333")
        addButton = view.findViewById(R.id.yesBtn)
        noButton = view.findViewById(R.id.noBtn)

        addButton.setOnClickListener {
            //when add btn pressed, store item to the db.
            startActivity(Intent(context,MainActivity::class.java))
        }

        noButton.setOnClickListener {
            //close dialog
        }
    }


    fun updateDialogUI(shopItem: ShopItem){

        txtItemDesc = myView.findViewById<View>(R.id.txt_item_description) as TextView
        txtItemPrice = myView.findViewById<View>(R.id.txt_item_price) as TextView
        itemImg = myView.findViewById<View>(R.id.item_image) as ImageView

        Log.d(TAG,"Dialog ShopItem: $shopItem")
        txtItemDesc.text= shopItem.description
        txtItemPrice.text = shopItem.price.toString()

        //code to load image
        Glide.with(itemImg.context).
        load("gs://the-busy-shop.appspot.com/apple.jpg").
        into(itemImg)

    }

    private fun storeItemToCart(item: ShopItem?) {

        cartEntity = CartEntity(0L, item?.description.toString(), item!!.image.toString(),
            item.price, 1)

        Log.d(TAG,"ITEM ENTITY: ${cartEntity.price}  ${cartEntity.description}")

        itemLocalRepository.insertCartItem(cartEntity)
        Toast.makeText(context,"Item added to a cart.", Toast.LENGTH_LONG).show()

    }



}