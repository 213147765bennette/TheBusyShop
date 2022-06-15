package com.ikhokha.techcheck.presentation.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.dto.OrderCreateDTO
import com.ikhokha.techcheck.data.entity.CartEntity
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.databinding.FragmentNotificationsBinding
import com.ikhokha.techcheck.presentation.adapter.ConfirmOrderAdapter
import com.ikhokha.techcheck.presentation.adapter.OrderSummaryAdapter
import com.ikhokha.techcheck.util.TodayDate.getTodayDateTime

class NotificationsFragment : Fragment() {

    companion object{
        private var TAG = "NotificationsFragment"
    }

    private var _binding: FragmentNotificationsBinding? = null
    private var orderCreateDTO: OrderCreateDTO = OrderCreateDTO()
    private  var orderTotal:Double = 0.0
    private lateinit var summaryAdapter: OrderSummaryAdapter
    private lateinit var orderItem: List<ShopItem>
    private lateinit var recyclerView: RecyclerView

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[NotificationsViewModel::class.java]

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun init(view: View){
        recyclerView = view.findViewById(R.id.summary_recycler_view)

        orderItem = ArrayList()

        summaryAdapter = OrderSummaryAdapter(orderItem)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        val args : NotificationsFragmentArgs by navArgs()
        if(args.orderDetails?.orders !==null){

            orderCreateDTO = args.orderDetails!!
            orderCreateDTO.orders?.forEach {
                orderTotal+=it.price
            }

            Log.d(TAG, "========= ORDER TOTAL ==========: $orderTotal")

            binding.txtTotalPrice.text = "R"+orderTotal.toString()+"0"
            binding.txtDate.text = getTodayDateTime()

            val items: List<ShopItem>? = orderCreateDTO.orders
            if (items != null) {
                orderItem = items
                summaryAdapter = OrderSummaryAdapter(items)
                summaryAdapter.setList(items)

                //inflate custom adapter here
                recyclerView.apply {
                    layoutManager = linearLayoutManager
                    adapter = summaryAdapter
                }

                //here making sure that the found record is displayed at the very first top
                recyclerView.post {
                    recyclerView.scrollToPosition(0)
                }

            }


            Log.d(TAG, "========= ORDER SUMMARY ==========:" + Gson().toJson(orderCreateDTO))
            binding.subMainSummaryLayout.isVisible = true
            binding.emptySummary.isVisible = false
        }else{
            binding.subMainSummaryLayout.isVisible = false
            binding.emptySummary.isVisible = true
        }

        _binding?.btnConfirm?.setOnClickListener {
            Log.d(TAG, "========= CONFIRMING ORDER ==========:")

            //val orderItems: String = "Order Items : ${orderCreateDTO.orders?.get(0)?.description}"
            //val totalPrice: String = "Total Price: R$orderTotal"
            //val orderDate: String = "Date: "+getTodayDateTime()
            //share The receipt
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND

                //orderItems+"\n" +
                /*putExtra(Intent.EXTRA_TEXT, "Thank you for shopping with us today, here is your receipt\n" +
                        totalPrice+"\n" +
                        orderDate)*/
                putExtra(Intent.EXTRA_TEXT, "Thank you for shopping with us today, here is your receipt")

                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}