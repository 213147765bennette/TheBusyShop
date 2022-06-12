package com.ikhokha.techcheck.presentation.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.ikhokha.techcheck.data.dto.OrderCreateDTO
import com.ikhokha.techcheck.databinding.FragmentNotificationsBinding
import com.ikhokha.techcheck.util.TodayDate.getTodayDateTime

class NotificationsFragment : Fragment() {

    companion object{
        private var TAG = "NotificationsFragment"
    }

    private var _binding: FragmentNotificationsBinding? = null
    private var orderCreateDTO: OrderCreateDTO = OrderCreateDTO()
    private  var orderTotal:Double = 0.0

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args : NotificationsFragmentArgs by navArgs()
        if(args.orderDetails?.orders !==null){
            orderCreateDTO = args.orderDetails!!
            orderCreateDTO.orders?.forEach {
                orderTotal+=it.price
            }
            Log.d(TAG, "========= ORDER TOTAL ==========: $orderTotal")
            binding.txtItemDescription.text = orderCreateDTO.orders?.get(0)?.description
            binding.txtTotalPrice.text = "R"+orderTotal.toString()+"0"
            binding.txtDate.text = getTodayDateTime()
            Log.d(TAG, "========= ORDER SUMMARY ==========:" + Gson().toJson(orderCreateDTO))
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}