package com.ikhokha.techcheck.presentation.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ikhokha.techcheck.MainActivity
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.databinding.FragmentHomeBinding
import com.ikhokha.techcheck.presentation.scan.ScannerActivity
import dagger.hilt.android.AndroidEntryPoint

class HomeFragment : Fragment() {

    lateinit var viewmodel: HomeViewModel
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
        viewmodel =
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
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {

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
