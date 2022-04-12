package com.djevannn.nftmarketplace.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djevannn.nftmarketplace.data.NFT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    private val _listNft = MutableLiveData<List<NFT>>()
    val listNft: LiveData<List<NFT>> = _listNft

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchAllNFT() {
        _isLoading.value = true

        val db = Firebase.database.reference
        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val res = ArrayList<NFT>()

                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(NFT::class.java)
                    Log.d("ViewModel", product.toString())
                    if (product != null) res.add(product)
                }

                _listNft.value = res
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("ViewModel", databaseError.toString())
            }
        }
        db.child("assets").addValueEventListener(nftListener)

        _isLoading.value = false
    }
}