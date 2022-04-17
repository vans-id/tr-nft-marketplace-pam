package com.djevannn.nftmarketplace.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.databinding.FragmentAddBinding
import com.djevannn.nftmarketplace.helper.uriToFile
import java.io.File

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private var getFile: File? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

        _binding = FragmentAddBinding.inflate(
            inflater,
            container,
            false
        )
        val root: View = binding.root

//        val textView: TextView = binding.textDashboard
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnUpload.setOnClickListener {
                startGallery()
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.search_image))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext().applicationContext)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}