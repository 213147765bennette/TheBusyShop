package com.ikhokha.techcheck.presentation.notifications

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.dto.OrderCreateDTO
import com.ikhokha.techcheck.data.model.ShopItem
import com.ikhokha.techcheck.databinding.FragmentNotificationsBinding
import com.ikhokha.techcheck.presentation.adapter.OrderSummaryAdapter
import com.ikhokha.techcheck.util.TodayDate.getTodayDateTime
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    companion object{
        private var TAG = "NotificationsFragment"
    }

    @Inject
    lateinit var factory: NotificationsViewModelFactory
    lateinit var notificationViewModel: NotificationsViewModel

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
        notificationViewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]

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
            showSuccessfulOrderDialog()

        }

    }

    private fun shareReceipt(){
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

    //show the successful dialog
    private fun showSuccessfulOrderDialog(){
        //remove all cart items added
        notificationViewModel.deleteAll()

        val dialog = MaterialDialog (requireContext())
            .cornerRadius(8f)
            .icon(R.drawable.success_one)
            .cancelable(false)
            .title(R.string.dialog_successful_order_title)
            .message(R.string.dialog_successful_order_msg)

        dialog.positiveButton(R.string.dialog_successful_order) {
            shareReceipt()
        }

        dialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}