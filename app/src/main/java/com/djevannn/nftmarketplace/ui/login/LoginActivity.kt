package com.djevannn.nftmarketplace.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.MainActivity
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.databinding.ActivityLoginBinding
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setViewModel()
        setAction()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            if(user.isLogin){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }
    }

    private fun setView() {
        supportActionBar?.hide()
    }

    private fun setAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                viewModel.checkUser(etUsername.editText?.text.toString(),etUsername.editText?.text.toString(),
                    object : ResponseCallback {
                        override fun getCallback(msg: String, status: Boolean) {
                            if(status){
                                Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            }
        }

    }
}