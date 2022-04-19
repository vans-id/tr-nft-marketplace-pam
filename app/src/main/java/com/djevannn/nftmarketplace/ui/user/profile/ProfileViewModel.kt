package com.djevannn.nftmarketplace.ui.user.profile

import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref:UserPreference) : ViewModel() {

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}