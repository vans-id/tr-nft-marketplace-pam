package com.djevannn.nftmarketplace.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.ResponseCallback
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginViewModel(private val pref: UserPreference) : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun checkUser(username:String, password:String, responseCallback: ResponseCallback) {
        _isLoading.value = true

        val db = Firebase.database.reference
        val userListener = object : ValueEventListener {
            override fun onDataChange(usernameSnapshot: DataSnapshot) {

                if(usernameSnapshot.exists()) {
                    db.child("password")
                        .equalTo(password)
                        .addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(passwordSnapshot: DataSnapshot) {
                                if(passwordSnapshot.exists()) {
                                    // cek password
                                    Log.d("CEK DATA ATAS", usernameSnapshot.toString())
                                    responseCallback.getCallback("User ditemukan!", true)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("onCanceledAtas", "onCancelled: $error")
                            }

                        })
                    Log.d("CEK DATA", usernameSnapshot.exists().toString())
//                    responseCallback.getCallback("User ditemukan!", true)
                }
                Log.d("CEK DATA", usernameSnapshot.exists().toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onCanceledBawah", "onCancelled: $error")
            }
        }

        db.child("users").orderByChild("username")
            .equalTo(username)
            .addValueEventListener(userListener)



//        val userListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val res = ArrayList<NFT>()
//                responseCallback.getCallback("Success", true)
//                for (snapshot in dataSnapshot.children) {
//                    val product = snapshot.getValue(NFT::class.java)
//                    Log.d("ViewModel", product.toString())
//                    if (product != null) res.add(product)
//                }
//
//                _listNft.value = res
//                _isLoading.value = false
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d("ViewModel", databaseError.toString())
//                _isLoading.value = false
//            }
//        }
//        db.child("users").addValueEventListener(userListener)


    }
}