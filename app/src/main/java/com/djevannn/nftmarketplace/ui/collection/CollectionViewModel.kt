package com.djevannn.nftmarketplace.ui.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.djevannn.nftmarketplace.data.NFT
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CollectionViewModel : ViewModel() {

    private val _collectionList = MutableLiveData<List<NFT>>()
    val collectionList: LiveData<List<NFT>> = _collectionList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchAllCollection()
    }

    private fun fetchAllCollection() {
        _isLoading.value = true

        val db = Firebase.database.reference
        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val res = ArrayList<NFT>()

                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(NFT::class.java)
                    if (product != null) res.add(product)
                }

                _collectionList.value = res
                _isLoading.value = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _isLoading.value = false
            }
        }
        // dapatkan username yang login disini
        db.child("assets").orderByChild("owner").equalTo("djevann")
            .addValueEventListener(nftListener)
    }

}