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

    private val _creator = MutableLiveData<Creator>()
    val creator: LiveData<Creator> = _creator

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getCreatorData(creatorWallet: String) {
        _isLoading.value = true

        val db = Firebase.database.reference
        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var res = Creator("","","","")

                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(Creator::class.java)
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
        db.child("creators").orderByChild("wallet")
            .equalTo(creatorWallet)
            .addValueEventListener(nftListener)
    }
}