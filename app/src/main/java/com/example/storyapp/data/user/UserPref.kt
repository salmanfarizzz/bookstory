package com.example.storyapp.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPref private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: User) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[NAME_KEY] = user.name
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[NAME_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPref? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPref {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}