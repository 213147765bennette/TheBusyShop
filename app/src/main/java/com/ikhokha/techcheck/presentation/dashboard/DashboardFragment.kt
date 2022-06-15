package com.ikhokha.techcheck.presentation.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.ikhokha.techcheck.MainActivity
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.dto.OrderCreateDTO
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.databinding.FragmentDashboardBinding
import com.ikhokha.techcheck.presentation.adapter.ConfirmOrderAdapter
import com.ikhokha.techcheck.util.CalculateTotal
import com.ikhokha.techcheck.util.CalculateTotal.getTotalPrice
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() , ConfirmOrderAdapter.RecycleViewItemClickInterface {
    companion object{
        private var TAG = "DashboardFragment"
    }

    @Inject
    lateinit var factory: DashboardViewModelFactory
    lateinit var dashboardViewModel: DashboardViewModel
    // This property is only valid between onCreateView and
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var orderAdapter: ConfirmOrderAdapter
    private lateinit var orderItem: List<CartEntity>
    private var orderCreateDTO: OrderCreateDTO = OrderCreateDTO()
    private lateinit var recyclerView:RecyclerView
    private  var isCartEmpty: Boolean = true

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    private val deleteClickListner: (data: CartEntity) ->Unit ={
        showDeleteDialog(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        dashboardViewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View){
        recyclerView = view.findViewById(R.id.confirm_order_recycler_view)

        orderItem = ArrayList()

        orderAdapter = ConfirmOrderAdapter(orderItem,this, deleteClickListner)

        observeDeletedItems()
        observerCartItems()

        _binding?.txtAddMoreItem?.setOnClickListener {

            Log.d(TAG,"========= Going to add more items ")
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)

        }

        _binding?.btnContinue?.setOnClickListener {

            //item data
            val bundle = Bundle().apply {
                putSerializable("order_details", orderCreateDTO)

            }

            if (isCartEmpty){
                Toast.makeText(context, "Your cart is empty, please add items first.", Toast.LENGTH_LONG).show()
            }else
            {
                findNavController().navigate(
                    R.id.action_navigation_dashboard_to_navigation_notifications,
                    bundle
                )
            }

        }
    }

    private fun observeDeletedItems(){
        dashboardViewModel.isDeleted.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Item successfully removed from cart.", Toast.LENGTH_LONG)
                    .show()

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observerCartItems() {

        dashboardViewModel.cartItemsList.observe(viewLifecycleOwner) {
            Log.d(TAG, "========= VIEWING LIVE DATA CART ITEMS ==========:" +Gson().toJson(it))

            if (it.isNotEmpty()){
                val items: List<CartEntity> = it
                orderItem = items
                isCartEmpty = false

                orderAdapter = ConfirmOrderAdapter(items,this,deleteClickListner)
                orderAdapter.setList(it)

                //inflate custom adapter here
                recyclerView.apply {
                    layoutManager = linearLayoutManager
                    adapter = orderAdapter
                }

                //here making sure that the found record is displayed at the very first top
                recyclerView.post {
                    recyclerView.scrollToPosition(0)
                }

                val newList = mutableListOf<ShopItem>()

                orderItem.forEach {response->
                    val shopItem = ShopItem("","","",0,0.0)

                    shopItem.quantity = response.quantity
                    shopItem.description = response.description
                    shopItem.price = response.price

                    newList += shopItem
                    orderCreateDTO.orders =  newList
                }

                _binding?.txtTotal?.text = "R"+calculateTotalAmount(items).toString()+"0"

                //Add more items
                _binding?.txtAddMoreItem?.isVisible = true
                _binding?.addItemsSign?.isVisible = true


            }else{
                isCartEmpty = true
                showEmptyCartDialog()
                //Add more items
                _binding?.txtAddMoreItem?.isVisible = false
                _binding?.addItemsSign?.isVisible = false
            }
        }

    }

    private fun calculateTotalAmount(orders: List<CartEntity>): Double{

        var orderTotalPrice = 0.0

        orders.forEach {
            orderTotalPrice+= it.price
        }

        Log.d(TAG,"=========Total Amount R$orderTotalPrice")

        return orderTotalPrice
    }

    //show the delete dialog
    private fun showDeleteDialog(data: CartEntity){

        val dialog = MaterialDialog (requireContext())
            .cornerRadius(8f)
            .cancelable(false)
            .title(R.string.dialog_delete_title)
            .message(R.string.dialog_delete_msg)


        dashboardViewModel.cartId.value = data.id

        dialog.positiveButton(R.string.dialog_delete) {
            dashboardViewModel.image.value = data.image
            dashboardViewModel.description.value = data.description
            dashboardViewModel.price.value = data.price
            dashboardViewModel.quantity.value = data.quantity

            //Delete the Cart Item record from RoomDB
            dashboardViewModel.delete()

        }

        dialog.negativeButton {
            dialog.dismiss()
        }

        dialog.show()

    }

    //show this  dialog when cart list is empty
    private fun showEmptyCartDialog(){

        val dialog = MaterialDialog(requireContext())
            .cornerRadius(8f)
            .cancelable(false)
            .title(R.string.empty_list)
            .message(R.string.empty_list_msg)

        dialog.positiveButton {
            dialog.dismiss()
        }

        dialog.show()

    }

    //show this  dialog when cart item is clicked
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("InflateParams", "RtlHardcoded", "SetTextI18n")
    fun showMoreInfoDialog(itemCode:String, desc:String, price:Double, quantity:Int,image:String){

        val popupWindow: PopupWindow
        val layoutInflater = LayoutInflater.from(context)
        val view:View = layoutInflater.inflate(R.layout.item_moreinfo_layout, null)

        val txtItemCode:TextView = view.findViewById(R.id.item_code)
        val txtItemName: TextView = view.findViewById(R.id.txt_item_name)
        val itemQuantity: TextView = view.findViewById(R.id.txt_item_quantity)
        val txtItemPrice: TextView = view.findViewById(R.id.txt_item_price)
        var itemImg: ImageView = view.findViewById(R.id.item_image)

        txtItemCode.text = itemCode
        txtItemName.text = "Item Name: $desc"
        itemQuantity.text = "Quantity: $quantity"
        //txtItemPrice.text = "Price: "+ getTotalPrice(price, quantity)
        txtItemPrice.text = "Price: R"+price+"0"
        //itemImg.setImageURI("")

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = 350

        popupWindow = PopupWindow(view, width, height, true)
        // display the popup in the center
        popupWindow.width
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onItemClick(data: CartEntity, position: Int) {
        Log.d(TAG,"=========Item: ${data.description} was clicked")
        showMoreInfoDialog(data.itemCode, data.description, data.price, data.quantity, data.image)
    }
}