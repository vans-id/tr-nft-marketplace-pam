package com.djevannn.nftmarketplace.ui.splash_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djevannn.nftmarketplace.helper.UserPreference

class SplashScreenViewModel(private val pref: UserPreference): ViewModel() {

    fun getThemeSetting(): LiveData<Int> {
        return pref.getThemeSetting().asLiveData()
    }
}