package com.djevannn.nftmarketplace.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.djevannn.nftmarketplace.databinding.ActivityRegisterBinding
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.ui.auth.login.LoginActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        playAnimation()
    }

    private fun playAnimation() {

        val titleTextView = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA,1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.etName, View.ALPHA,1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.etUsername, View.ALPHA,1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA,1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA,1f).setDuration(500)
        val tvTxtRegist = ObjectAnimator.ofFloat(binding.tvTxtLogin, View.ALPHA,1f).setDuration(500)
        val tvRegist = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(tvTxtRegist,tvRegist)
        }

        AnimatorSet().apply {
            playSequentially(titleTextView,nameEditTextLayout,emailEditTextLayout,passwordEditTextLayout,loginBtn,together)
            start()
        }
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
                var isError = false
                if(TextUtils.isEmpty(etName.editText?.text)){
                    isError = true
                    etName.error = getString(R.string.name_error_field)
                }

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
                    cekUser()
                }
            }
        }
    }

    private fun cekUser(): Boolean {
        var isFounds = false
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("username").value == binding.etUsername.editText?.text.toString()) {
                        isFounds = true
                    }
                }
                if(!isFounds){
                    viewModel.saveUser(
                        binding.etName.editText?.text.toString(),
                        binding.etUsername.editText?.text.toString(),
                        binding.etPassword.editText?.text.toString(),
                        object : ResponseCallback {
                            override fun getCallback(
                                msg: String,
                                status: Boolean
                            ) {
                                showDialogs(msg, status)
                            }
                        })
                }else {
                    showDialogs(getString(R.string.register_error_user_found), false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Get Data bawah", error.toString())
            }
        })
        return isFounds
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
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Oops")
                val message = getString(R.string.register_error, msg)
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