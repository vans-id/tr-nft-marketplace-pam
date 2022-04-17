package com.djevannn.nftmarketplace.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.UserPreference

class ProfileViewModel(private val pref:UserPreference) : ViewModel() {

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}