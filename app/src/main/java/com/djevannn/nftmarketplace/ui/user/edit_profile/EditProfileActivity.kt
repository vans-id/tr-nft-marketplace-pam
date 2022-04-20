package com.djevannn.nftmarketplace.ui.user.edit_profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.djevannn.nftmarketplace.MainActivity
import com.djevannn.nftmarketplace.R
import com.djevannn.nftmarketplace.ViewModelFactory
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.data.UserRegist
import com.djevannn.nftmarketplace.databinding.ActivityEditProfileBinding
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private lateinit var user: User
    private lateinit var key : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.btnSave.setOnClickListener {
            saveUser()
        }
    }

    private fun saveUser() {
        var isFounds = false
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("username").value == user.username) {
                        key = data.key.toString()
                        isFounds = true
                    }
                }
                if(isFounds){
                    val userRegist = UserRegist(
                        about = binding.etAbout.editText?.text.toString(),
                        balance = user.balance,
                        name = binding.etName.editText?.text.toString(),
                        username = user.username,
                        password = user.password,
                        wallet = binding.etWallet.editText?.text.toString(),
                        created_at = user.created_at,
                        photo_url = binding.etUrlPhoto.editText?.text.toString(),
                    )

                    viewModel.updateUser(
                        userRegist,
                        key,
                        object : ResponseCallback {
                            override fun getCallback(
                                msg: String,
                                status: Boolean
                            ) {
                                showDialogs(msg, status)
                            }
                        })
                }else {
                    showDialogs("User tidak ada!", false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Get Data bawah", error.toString())
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[EditProfileViewModel::class.java]

        viewModel.getUser().observe(this) {
            this.user = it
            binding.apply {
                etName.editText?.setText(it.name)
                etAbout.editText?.setText(it.about)
                etUrlPhoto.editText?.setText(it.photo_url)
                etWallet.editText?.setText(it.wallet)
                tvName.text = it.name
                Glide.with(root)
                    .load(it.photo_url)
                    .apply(RequestOptions().override(300, 300))
                    .into(ivAvatar)
            }
        }
    }

    private fun setupView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }

    private fun showDialogs(msg: String, status: Boolean) {
        if (status) {
            AlertDialog.Builder(this).apply {
                setTitle("Yay !")
                val message = getString(R.string.edit_profile_success)
                setMessage(message)
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Oops")
                val message = getString(R.string.edit_profile_error, msg)
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