package com.djevannn.nftmarketplace.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.FirebaseDatabase

class AddViewModel(val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun saveNFT(
        title: String,
        urlPhoto: String,
        price: Double,
        user: User,
        token: Int,
        callback: ResponseCallback
    ) {
        _isLoading.value = true

        val ref = FirebaseDatabase.getInstance().getReference("assets")
        val userId = ref.push().key
        val nft = NFT(
            creator = user.username,
            current_price = price,
            image_url = urlPhoto,
            owner = user.username,
            title = "$title #",
            token_id = token,
        )
        if (userId != null) {
            ref.child(token.toString()).setValue(nft).apply {
                addOnCompleteListener {
                    callback.getCallback("", true)
                }
                addOnCanceledListener {
                    callback.getCallback("", false)
                }
            }
            _isLoading.value = false
        }
        _isLoading.value = false

    }
}