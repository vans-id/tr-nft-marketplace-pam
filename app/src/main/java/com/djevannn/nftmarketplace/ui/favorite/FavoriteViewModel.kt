package com.djevannn.nftmarketplace.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djevannn.nftmarketplace.data.NFT

class FavoriteViewModel: ViewModel() {

    private val _favoriteList = MutableLiveData<List<NFT>>()
    val favoriteList: LiveData<List<NFT>> = _favoriteList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchAllFavorites() {
        // shared data
    }

}