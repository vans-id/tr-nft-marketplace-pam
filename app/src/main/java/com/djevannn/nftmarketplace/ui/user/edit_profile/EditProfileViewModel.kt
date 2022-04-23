package com.djevannn.nftmarketplace.ui.user.edit_profile

import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.data.UserRegist
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class EditProfileViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun updateUser(
        userRegist: UserRegist,
        key : String,
        callback: ResponseCallback
    ) {
        _isLoading.value = true

        val ref = FirebaseDatabase.getInstance().getReference("users")

        ref.child(key).setValue(userRegist).apply {
            addOnCompleteListener {
                val user = User(
                    about = userRegist.about,
                    balance = userRegist.balance,
                    name = userRegist.name,
                    username = userRegist.username,
                    password = userRegist.password,
                    wallet =  userRegist.wallet,
                    created_at = userRegist.created_at,
                    photo_url = userRegist.photo_url,
                    isLogin = true
                )
                saveUser(user)
                callback.getCallback("", true)
            }
            addOnCanceledListener {
                callback.getCallback("", false)
            }
        }
        _isLoading.value = false

    }

    private fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}