package com.djevannn.nftmarketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.setting.SettingViewModel
import com.djevannn.nftmarketplace.ui.user.collection.CollectionViewModel
import com.djevannn.nftmarketplace.ui.main.detail.DetailViewModel
import com.djevannn.nftmarketplace.ui.user.edit_profile.EditProfileViewModel
import com.djevannn.nftmarketplace.ui.user.favorite.FavoriteViewModel
import com.djevannn.nftmarketplace.ui.auth.login.LoginViewModel
import com.djevannn.nftmarketplace.ui.user.profile.ProfileViewModel
import com.djevannn.nftmarketplace.ui.auth.register.RegisterViewModel
import com.djevannn.nftmarketplace.ui.user.user_nft.NFTUserViewModel

class ViewModelFactory(private val pref: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(NFTUserViewModel::class.java) -> {
                NFTUserViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(pref) as T
            }
            modelClass.isAssignableFrom(CollectionViewModel::class.java) -> {
                CollectionViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(pref) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}