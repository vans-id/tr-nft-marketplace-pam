package com.djevannn.nftmarketplace.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djevannn.nftmarketplace.data.Creator
import com.djevannn.nftmarketplace.data.NFT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailViewModel : ViewModel() {

    private val _detailNFT = MutableLiveData<NFT>()
    val detailNFT: LiveData<NFT> = _detailNFT

    private val _creator = MutableLiveData<Creator>()
    val creator: LiveData<Creator> = _creator

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val db = Firebase.database.reference

    fun buyNFT(item: NFT) {
        _isLoading.value = true
        item.owner = "djevann"

        val db = Firebase.database.reference
        db.child("assets").child(item.token_id.toString())
            .setValue(item).addOnSuccessListener {
                _message.value = "Berhasil membeli NFT"
                _isLoading.value = false
            }.addOnFailureListener {
                _message.value = "Gagal membeli NFT: ${it.message}"
                _isLoading.value = false
            }
    }

    fun getCreatorData(creator: String) {
        _isLoading.value = true

        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var res = Creator("", "", "")

                for (snapshot in dataSnapshot.children) {
                    val product =
                        snapshot.getValue(Creator::class.java)
                    Log.d("ViewModelDetail", product.toString())
                    if (product != null) res = product
                }

                _creator.value = res
                _isLoading.value = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("ViewModelDetail", databaseError.toString())
                _isLoading.value = false
            }
        }
        db.child("creators").orderByChild("name")
            .equalTo(creator)
            .addValueEventListener(nftListener)
    }

    fun onFavoriteClicked(item: NFT) {
        when (_isFavorite.value) {
            true -> {
                removeFromFavorite(item)
                _isFavorite.value = false
            }
            false -> {
                addToFavorite(item)
                _isFavorite.value = true
            }
            else -> return
        }
    }

    fun getFavoriteStatus(item: NFT) {
        // get user here
        db.child("favorites").child("djevann")
            .child(item.token_id.toString()).get()
            .addOnSuccessListener {
                _isFavorite.value =
                    item == it.getValue(NFT::class.java)
            }
            .addOnSuccessListener {
                Log.d("DetailViewModel", it.toString())
            }
    }

    private fun addToFavorite(item: NFT) {
        // get user here
        db.child("favorites")
            .child("djevann")
            .child(item.token_id.toString()).setValue(item)
            .addOnSuccessListener {
                _message.value = "Berhasil menambahkan ke favorit"
            }
            .addOnFailureListener {
                _message.value = "Gagal menambahkan ke favorit"
            }
    }

    private fun removeFromFavorite(item: NFT) {
        db.child("favorites")
            .child("djevann")
            .child(item.token_id.toString()).removeValue()
            .addOnSuccessListener {
                _message.value = "Berhasil menghapus dari favorit"
            }
            .addOnFailureListener {
                _message.value = "Gagal menghapus dari favorit"
            }
    }


}