package com.djevannn.nftmarketplace.ui.main.user_nft

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.data.UserRegist
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NFTUserViewModel(private val pref: UserPreference): ViewModel() {

    private val _userNFT = MutableLiveData<UserRegist>()
    val userNFT: LiveData<UserRegist> = _userNFT

    private val _collectionList = MutableLiveData<List<NFT>>()
    val collectionList: LiveData<List<NFT>> = _collectionList

    fun getUserNFT(username: String){
        FirebaseDatabase.getInstance().getReference("users")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var res = UserRegist("", 0.0, "","","","","","")
                    for (data in snapshot.children) {
                        if (data.child("username").value == username) {
                            Log.d("GET DATA", "onDataChange: $data")
                            val userNFTs = UserRegist(
                                about = data.child("about").value.toString(),
                                balance = data.child("balance").value.toString().toDouble(),
                                created_at = data.child("created_at").value.toString(),
                                name = data.child("name").value.toString(),
                                password = data.child("password").value.toString(),
                                photo_url = data.child("photo_url").value.toString(),
                                username = data.child("username").value.toString(),
                                wallet = data.child("wallet").value.toString(),
                            )
                            _userNFT.value = userNFTs
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Transfer Error 1", error.toString())
                }
            })
    }

    fun fetchAllCollection(username: String) {

        val db = Firebase.database.reference
        val nftListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val res = ArrayList<NFT>()

                for (snapshot in dataSnapshot.children) {
                    val product = snapshot.getValue(NFT::class.java)
                    if (product != null) res.add(product)
                }

                _collectionList.value = res
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("OnCanceled", "onCancelled: $databaseError")
            }
        }
        // dapatkan username yang login disini
        db.child("assets").orderByChild("owner")
            .equalTo(username)
            .addValueEventListener(nftListener)
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }
}