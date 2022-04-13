package com.djevannn.nftmarketplace.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewModel : ViewModel() {

    private val _textTitle = MutableLiveData<String>().apply {
        value = "This is Add Fragment"
    }
    val textTitle: LiveData<String> = _textTitle
}