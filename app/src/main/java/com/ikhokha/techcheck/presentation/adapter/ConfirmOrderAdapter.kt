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
import com.ikhokha.techcheck.data.response.ConfirmOrderResponse

/**
 * Created by Bennette Molepo on 04/06/2022.
 */
class ConfirmOrderAdapter(var cartResponse: List<CartEntity>, private val deleteClickListener: (data: CartEntity) -> Unit)
    :  RecyclerView.Adapter<ConfirmOrderAdapter.OrderItemViewHolder>(){

    companion object{
        private val  TAG ="ConfirmOrderAdapter"
    }

    //A smart way of updating my recycleview
    private val diffUtil = object : DiffUtil.ItemCallback<CartEntity>(){
        override fun areItemsTheSame( oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CartEntity, newItem: CartEntity): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this,diffUtil)

    //getting the list size here
    override fun getItemCount(): Int = differ.currentList.size

    fun setList(itemList: List<CartEntity>){
        differ.submitList(itemList)
    }


    //inflating the layout that will be shown to the client on cart screen
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_order_cart_layout,parent,false)
        return OrderItemViewHolder(view,deleteClickListener)
    }

    //bind my data to the holder
    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        Log.d(TAG,"=========== AM BINDING  LIST OF ORDER ITEMS TO MY VIEW ===========")
        holder.bind(cartResponse[position])
    }

    class OrderItemViewHolder(itemView: View,
                              private val deleteClickListener: (data: CartEntity) -> Unit)
        : RecyclerView.ViewHolder(itemView){

        private val txtQuantity = itemView.findViewById<TextView>(R.id.txt_quantity)
        private val txtDescription = itemView.findViewById<TextView>(R.id.txtDescription)
        private val txtTotalAmnt = itemView.findViewById<TextView>(R.id.txt_totalAmnt)
        //delete button
        private val deleteButton = itemView.findViewById<ShapeableImageView>(R.id.fab_favourites)


        @SuppressLint("SetTextI18n")
        fun bind(data: CartEntity){

            Log.d(TAG,"Binding data $data")

            txtQuantity.text = data.quantity.toString()
            txtDescription.text = data.description
            txtTotalAmnt.text = "R"+data.price.toString()+"0"

            deleteButton.setOnClickListener {
                deleteClickListener(data)
            }

        }
    }

}