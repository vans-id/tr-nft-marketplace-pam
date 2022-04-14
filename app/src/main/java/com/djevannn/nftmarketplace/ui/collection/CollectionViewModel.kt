package com.djevannn.nftmarketplace.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djevannn.nftmarketplace.data.NFT

class CollectionViewModel : ViewModel() {

    private val _collectionList = MutableLiveData<List<NFT>>()
    val collectionList: LiveData<List<NFT>> = _collectionList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchAllCollection() {
        // firebase
    }

}