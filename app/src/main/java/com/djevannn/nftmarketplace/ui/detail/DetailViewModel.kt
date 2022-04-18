package com.djevannn.nftmarketplace.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djevannn.nftmarketplace.data.Creator
import com.djevannn.nftmarketplace.data.NFT
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.helper.UserPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel(private val pref: UserPreference) :
    ViewModel() {

    private val _creator = MutableLiveData<Creator>()
    val creator: LiveData<Creator> = _creator

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _isMine = MutableLiveData<Boolean>()
    val isMine: LiveData<Boolean> = _isMine

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val db = Firebase.database.reference
    private lateinit var user: User

    init {
        viewModelScope.launch {
            pref.getUser().collect {
                user = it
            }
        }

    }

    fun buyNFT(item: NFT) {
        _isLoading.value = true
        if (user.balance < item.current_price) {
            _message.value = "Gagal membeli NFT: Balance not enough"
            _isLoading.value = false
        } else {
            // get user here
            val previousOwner = item.owner
            val newOwner = user.username
            item.owner = newOwner

            val db = Firebase.database.reference
            db.child("assets").child(item.token_id.toString())
                .setValue(item).addOnSuccessListener {
                    transferBalance(
                        previousOwner,
                        newOwner,
                        item.current_price
                    )
                    _message.value = "Berhasil membeli NFT"
                    _isLoading.value = false
                }.addOnFailureListener {
                    _message.value =
                        "Gagal membeli NFT: ${it.message}"
                    _isLoading.value = false
                }


        }
    }

    private fun transferBalance(
        previousOwner: String,
        newOwner: String,
        currentPrice: Double
    ) {
        FirebaseDatabase.getInstance().getReference("users")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        if (data.child("username").value == newOwner) {
                            val balance =
                                data.child("balance").value.toString()
                                    .toDouble()
                            data.ref.child("balance")
                                .setValue(balance - currentPrice)
                            // update user

                            val user = User(
                                about = user.about,
                                balance = balance - currentPrice,
                                name = user.name,
                                username = user.username,
                                password = user.password,
                                wallet = user.wallet,
                                created_at = user.created_at,
                                photo_url = user.photo_url,
                                isLogin = true
                            )

                            saveUser(user)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Transfer Error 1", error.toString())
                }
            })

        FirebaseDatabase.getInstance().getReference("users")
            .addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        if (data.child("username").value == previousOwner) {
                            val balance =
                                data.child("balance").value.toString()
                                    .toDouble()
                            data.ref.child("balance")
                                .setValue(balance + currentPrice)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Transfer Error 2", error.toString())
                }
            })
    }

    private fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
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
        db.child("users").orderByChild("username")
            .equalTo(creator)
            .addValueEventListener(nftListener)
    }

    fun checkMine(item: NFT) {
        _isMine.value = item.owner == user.username
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
        db.child("favorites").child(user.username)
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
            .child(user.username)
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
            .child(user.username)
            .child(item.token_id.toString()).removeValue()
            .addOnSuccessListener {
                _message.value = "Berhasil menghapus dari favorit"
            }
            .addOnFailureListener {
                _message.value = "Gagal menghapus dari favorit"
            }
    }


}