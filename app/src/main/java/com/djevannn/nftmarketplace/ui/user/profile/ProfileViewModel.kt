package com.djevannn.nftmarketplace.ui.user.profile

import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.UserPreference
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel(private val pref: UserPreference) :
    ViewModel() {
    private val _getLocale = MutableLiveData<String>()
    val getLocale: LiveData<String> = _getLocale

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _getLocale.value = LocaleList.getDefault().get(0).language
        } else {
            _getLocale.value = Locale.getDefault().language
        }
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}