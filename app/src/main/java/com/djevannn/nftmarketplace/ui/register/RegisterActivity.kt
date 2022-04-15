package com.djevannn.nftmarketplace.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.MainActivity
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.databinding.ActivityRegisterBinding
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        MainActivity::class.java
                    )
                )
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        LoginActivity::class.java
                    )
                )
            }

            btnRegister.setOnClickListener {
                viewModel.saveUser(
                    etName.editText?.text.toString(),
                    etUsername.editText?.text.toString(),
                    etPassword.editText?.text.toString(),
                    object : ResponseCallback {
                        override fun getCallback(
                            msg: String,
                            status: Boolean
                        ) {
                            showDialogs(msg, status)
                        }
                    })
            }
        }
    }

    private fun showDialogs(msg: String, status: Boolean) {
        if (status) {
            AlertDialog.Builder(this).apply {
                setTitle("Yay !")
                val message = getString(R.string.register_success)
                setMessage(message)
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent =
                        Intent(context, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Oops")
                val message = getString(R.string.register_error)
                setMessage(message)
                setNegativeButton(getString(R.string.repeat)) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }
}