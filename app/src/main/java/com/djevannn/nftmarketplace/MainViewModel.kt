package com.djevannn.nftmarketplace

import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.UserPreference
import kotlinx.coroutines.launch

class MainViewModel (private val pref: UserPreference) : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun getThemeSetting(): LiveData<Int> {
        return pref.getThemeSetting().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}