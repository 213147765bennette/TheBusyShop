package com.ikhokha.techcheck.presentation.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.MaterialDialog
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.databinding.FragmentDashboardBinding
import com.ikhokha.techcheck.presentation.adapter.ConfirmOrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {
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
    private  var isCartEmpty: Boolean = true

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
        orderAdapter = ConfirmOrderAdapter(deleteClickListner)

        observeDeletedItems()
        observerCartItems()

        _binding?.btnContinue?.setOnClickListener {
            if (isCartEmpty){
                Toast.makeText(context, "Your cart is empty, please add items first.", Toast.LENGTH_LONG).show()
            }else
            {
                //transit to order summary screen
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
            Log.d(TAG, "========= VIEWING LIVE DATA CART ITEMS ==========: $it")

            if (it.isNotEmpty()){
                orderItem = it
                isCartEmpty = false
                Log.d(TAG,"************ ORDER ITEMS ***********: $orderItem")


                _binding?.txtTotal?.text = "R"+calculateTotalAmount(orderItem).toString()+"0"

                _binding?.txtAddMoreItem?.isVisible = true
                _binding?.addItemsSign?.isVisible = true

                orderAdapter.setList(it)

            }else{
                isCartEmpty = true
                showEmptyCartDialog()
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

            //then refresh the total
            //minusItemPriceFromTotal(data.price)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}