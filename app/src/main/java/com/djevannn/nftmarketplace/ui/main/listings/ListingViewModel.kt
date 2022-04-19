package com.djevannn.nftmarketplace.ui.main.listings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djevannn.nftmarketplace.data.Listing
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListingViewModel : ViewModel() {
    private val _listings = MutableLiveData<List<Listing>>()
    val listings: LiveData<List<Listing>> = _listings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchNFTListings(tokenId: Int) {
        _isLoading.value = true

        val db = Firebase.database.reference
        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val res = ArrayList<Listing>()

                for (snapshot in dataSnapshot.children) {
                    val product =
                        snapshot.getValue(Listing::class.java)
                    if (product != null) res.add(product)
                }

                _listings.value = res
                _isLoading.value = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _isLoading.value = false
                Log.d("ListingViewModel", databaseError.toString())
            }
        }
        // dapatkan username yang login disini
        db.child("history").orderByChild("nft_token_id")
            .equalTo(tokenId.toDouble())
            .addValueEventListener(nftListener)
    }
}