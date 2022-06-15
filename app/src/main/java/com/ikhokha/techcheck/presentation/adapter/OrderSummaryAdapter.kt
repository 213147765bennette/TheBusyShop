package com.ikhokha.techcheck.presentation.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.data.response.ConfirmOrderResponse
import com.ikhokha.techcheck.util.CalculateTotal
import com.ikhokha.techcheck.util.CalculateTotal.getTotalPrice

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
class OrderSummaryAdapter(var cartResponse: List<ShopItem>)
    :  RecyclerView.Adapter<OrderSummaryAdapter.OrderItemViewHolder>(){

    companion object{
        private val  TAG ="OrderSummaryAdapter"
    }

    //A smart way of updating my recycleview
    private val diffUtil = object : DiffUtil.ItemCallback<ShopItem>(){
        override fun areItemsTheSame( oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.itemCode == newItem.itemCode
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this,diffUtil)

    //getting the list size here
    override fun getItemCount(): Int = differ.currentList.size

    fun setList(itemList: List<ShopItem>){
        differ.submitList(itemList)
    }


    //inflating the layout that will be shown to the client on cart screen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_summary_layout,parent,false)
        return OrderItemViewHolder(view)
    }

    //bind my data to the holder
    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        Log.d(TAG,"=========== AM BINDING  LIST OF ORDER ITEMS TO MY VIEW ===========")
        holder.bind(cartResponse[position])
    }

    class OrderItemViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){

        private val txtQuantity = itemView.findViewById<TextView>(R.id.txt_quantity)
        private val txtDescription = itemView.findViewById<TextView>(R.id.txtDescription)
        private val txtPrice = itemView.findViewById<TextView>(R.id.txt_item_price)

        @SuppressLint("SetTextI18n")
        fun bind(data: ShopItem){

            Log.d(TAG,"Binding summary data $data")

            txtQuantity.text = data.quantity.toString()
            txtDescription.text = data.description
            //txtPrice.text = getTotalPrice(data.price, data.quantity)
            txtPrice.text = "R" + data.price.toString() + "0"

        }
    }


}