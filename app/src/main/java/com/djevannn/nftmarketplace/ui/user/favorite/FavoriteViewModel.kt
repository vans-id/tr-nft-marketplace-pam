package com.djevannn.nftmarketplace.ui.user.favorite

import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoriteViewModel(private val pref: UserPreference) : ViewModel() {

    private val _favoriteList = MutableLiveData<List<NFT>>()
    val favoriteList: LiveData<List<NFT>> = _favoriteList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var user: User

    init {
        viewModelScope.launch {
            pref.getUser().collect {
                user = it
                fetchAllFavorites()
            }
        }

    }

    private fun fetchAllFavorites() {
        _isLoading.value = true

        val db = Firebase.database.reference
        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val res = ArrayList<NFT>()

                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(NFT::class.java)
                    if (product != null) res.add(product)
                }

                _favoriteList.value = res
                _isLoading.value = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                _isLoading.value = false
            }
        }
        // dapatkan username yang login disini
        db.child("favorites").child(user.username)
            .addValueEventListener(nftListener)
    }

}