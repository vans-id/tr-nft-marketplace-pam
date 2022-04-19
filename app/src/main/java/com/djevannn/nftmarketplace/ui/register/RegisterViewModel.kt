package com.djevannn.nftmarketplace.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.data.UserRegist
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.helper.getCurrentDate
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel(private val pref: UserPreference) :
    ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(
        name: String,
        username: String,
        password: String,
        callback: ResponseCallback
    ) {
        _isLoading.value = true

        val dateInString = getCurrentDate()
        val ref = FirebaseDatabase.getInstance().getReference("users")
        val userId = ref.push().key
        val user = UserRegist(
            name = name,
            username = username,
            password = password,
            created_at = dateInString
        )
        if (userId != null) {
            ref.child(userId).setValue(user).apply {
                addOnCompleteListener {
                    callback.getCallback("", true)
                }
                addOnCanceledListener {
                    callback.getCallback("", false)
                }
            }
            _isLoading.value = false
        }
        _isLoading.value = false

    }
}