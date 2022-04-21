package com.djevannn.nftmarketplace.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.MainActivity
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.databinding.ActivityLoginBinding
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.ui.auth.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)

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
        playAnimation()
    }

    private fun playAnimation() {
        val titleTextView = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA,1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.etUsername, View.ALPHA,1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA,1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA,1f).setDuration(500)
        val tvTxtRegist = ObjectAnimator.ofFloat(binding.tvTxtRegister, View.ALPHA,1f).setDuration(500)
        val tvRegist = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(tvTxtRegist,tvRegist)
        }

        AnimatorSet().apply {
            playSequentially(titleTextView,emailEditTextLayout,passwordEditTextLayout,loginBtn,together)
            start()
        }
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        viewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                )
            }
        }
    }

    private fun setView() {
        supportActionBar?.hide()
    }

    private fun setAction() {
        binding.apply {
            showLoading()
            btnLogin.setOnClickListener {
                var isError = false

                if(TextUtils.isEmpty(etUsername.editText?.text)){
                    isError = true
                    etUsername.error = getString(R.string.username_empty)
                }
                if(TextUtils.isEmpty(etPassword.editText?.text)){
                    isError = true
                    etPassword.error = getString(R.string.password_empty)
                } else if(etPassword.editText?.text?.length!! < 6){
                    isError = true
                    etPassword.error = getString(R.string.password_error_field)
                }
                if(!isError){
                    viewModel.checkUser(etUsername.editText?.text.toString(),
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
            tvRegister.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        RegisterActivity::class.java
                    )
                )
            }
        }
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            binding.apply {
                when {
                    it -> progressBar.visibility = View.VISIBLE
                    else -> progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showDialogs(msg: String, status: Boolean) {
        if (status) {
            AlertDialog.Builder(this).apply {
                setTitle("Yay !")
                val message = getString(R.string.login_success)
                setMessage(message)
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Oops")
                val message = getString(R.string.login_error)
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