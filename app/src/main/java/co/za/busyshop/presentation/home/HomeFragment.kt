package co.za.busyshop.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.za.busyshop.databinding.FragmentHomeBinding
import co.za.busyshop.presentation.ScannerActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val openScanBtn: AppCompatButton = binding.openScanBtn
       openScanBtn.setOnClickListener {
           startActivity(Intent(context, ScannerActivity::class.java))
       }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}