package com.djevannn.nftmarketplace.ui.auth.login

import android.util.Log
import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun checkUser(
        username: String,
        password: String,
        responseCallback: ResponseCallback
    ) {
        var isFound = false
        _isLoading.value = true
        val db = FirebaseDatabase.getInstance().getReference("users")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("username").value == username && data.child(
                            "password"
                        ).value == password
                    ) {
                        val user = User(
                            about = data.child("about").value.toString(),
                            balance = data.child("balance").value.toString()
                                .toDouble(),
                            name = data.child("name").value.toString(),
                            username = data.child("username").value.toString(),
                            password = data.child("password").value.toString(),
                            wallet = data.child("wallet").value.toString(),
                            created_at = data.child("created_at").value.toString(),
                            photo_url = data.child("photo_url").value.toString(),
                            isLogin = true,
                        )
                        saveUser(user)
                        isFound = true
                        _isLoading.value = false
                    }
                }

                if (isFound) {
                    responseCallback.getCallback(
                        "User ditemukan!",
                        true
                    )
                    _isLoading.value = false
                } else {
                    responseCallback.getCallback(
                        "User tidak ditemukan! / salah",
                        false
                    )
                    _isLoading.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Get Data bawah", error.toString())
                responseCallback.getCallback(
                    "User tidak ditemukan!",
                    false
                )
                _isLoading.value = false
            }
        })


    }

    private fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

}