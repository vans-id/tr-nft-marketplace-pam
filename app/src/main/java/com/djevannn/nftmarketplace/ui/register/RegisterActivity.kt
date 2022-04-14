package com.djevannn.nftmarketplace.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.djevannn.nftmarketplace.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}