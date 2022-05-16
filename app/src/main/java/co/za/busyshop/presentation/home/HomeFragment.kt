package co.za.busyshop.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.za.busyshop.databinding.FragmentHomeBinding
import co.za.busyshop.presentation.ScannerActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val REQUEST_CAMERA_PERMISSION = 101

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

        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA
            )!= PackageManager.PERMISSION_GRANTED){
            askCameraPermission()
        }else{
            setUpControllers()
        }


        return root
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty()){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setUpControllers()
            }else{
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpControllers() {
        val openScanBtn: AppCompatButton = binding.openScanBtn
        openScanBtn.setOnClickListener {
            startActivity(Intent(context, ScannerActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}