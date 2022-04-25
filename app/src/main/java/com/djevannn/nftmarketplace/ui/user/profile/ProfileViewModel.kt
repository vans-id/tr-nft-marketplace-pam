package com.djevannn.nftmarketplace.ui.user.profile

import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.lifecycle.*
import com.djevannn.nftmarketplace.data.User
import com.djevannn.nftmarketplace.data.UserRegist
import com.djevannn.nftmarketplace.helper.UserPreference
import com.djevannn.nftmarketplace.helper.generateETHWallet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.*

class ProfileViewModel(private val pref: UserPreference) :
    ViewModel() {
    private val _getLocale = MutableLiveData<String>()
    val getLocale: LiveData<String> = _getLocale

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _getLocale.value = LocaleList.getDefault().get(0).language
        } else {
            _getLocale.value = Locale.getDefault().language
        }
    }

    fun resetMessage() {
        _message.value = ""
    }

    fun topUp(
        currUser: User,
        topUpBalance: Double = 1000.0
    ) {
        _isLoading.value = true

        val db = Firebase.database.reference
        val topUpListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val key = it.key.toString()
                    val newUser = currUser.copy()

                    newUser.balance += topUpBalance

                    val saveUser = UserRegist(
                        name = newUser.name,
                        username = newUser.username,
                        password = newUser.password,
                        created_at = newUser.created_at,
                        wallet = newUser.wallet,
                        about = newUser.about,
                        balance = newUser.balance,
                        photo_url = newUser.photo_url
                    )

                    db.child("users").child(key).setValue(
                        saveUser
                    ).addOnSuccessListener {
                        saveUser(newUser)
                        _message.value = "Berhasil melakukan topup"
                        _isLoading.value = false

                    }.addOnFailureListener {
                        _message.value = "Gagal melakukan topup"
                        _isLoading.value = false
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("ProfileViewModel", error.toString())
                _isLoading.value = false
            }
        }
        db.child("users").orderByChild("wallet")
            .equalTo(currUser.wallet)
            .addListenerForSingleValueEvent(topUpListener)
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    private fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }
}