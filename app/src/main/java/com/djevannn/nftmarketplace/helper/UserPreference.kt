package com.djevannn.nftmarketplace.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.djevannn.nftmarketplace.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[ABOUT] ?: "",
                preferences[BALANCE] ?: 0.0,
                preferences[CREATED_AT] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[PHOTO_URL] ?: "",
                preferences[USERNAME_KEY] ?: "",
                preferences[WALLET_KEY] ?: "",
                preferences[LOGIN_STATUS] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[USERNAME_KEY] = user.username
            preferences[PASSWORD_KEY] = user.password
            preferences[WALLET_KEY] = user.wallet
            preferences[CREATED_AT] = user.created_at
            preferences[PHOTO_URL] = user.photo_url
            preferences[LOGIN_STATUS] = user.isLogin
            preferences[ABOUT] = user.about
            preferences[BALANCE] = user.balance
        }
    }

    fun getThemeSetting(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: 0
        }
    }

    suspend fun saveThemeSetting(tema: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = tema
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[LOGIN_STATUS] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = ""
            preferences[USERNAME_KEY] = ""
            preferences[PASSWORD_KEY] = ""
            preferences[WALLET_KEY] = ""
            preferences[PHOTO_URL] = ""
            preferences[CREATED_AT] = ""
            preferences[LOGIN_STATUS] = false
            preferences[ABOUT] = ""
            preferences[BALANCE] = 0.0
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val LOGIN_STATUS =
            booleanPreferencesKey("login_status")
        private val WALLET_KEY = stringPreferencesKey("wallet")
        private val PHOTO_URL = stringPreferencesKey("photo")
        private val CREATED_AT = stringPreferencesKey("created_at")
        private val ABOUT = stringPreferencesKey("about")
        private val BALANCE = doublePreferencesKey("balance")

        private val THEME_KEY = intPreferencesKey("theme_setting")


        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}