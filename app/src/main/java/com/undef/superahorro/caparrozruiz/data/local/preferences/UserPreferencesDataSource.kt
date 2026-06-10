package com.undef.superahorro.caparrozruiz.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.undef.superahorro.caparrozruiz.data.model.User
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferencesDataSource(private val context: Context) {

    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_CITY = stringPreferencesKey("user_city")
        val MONTHLY_BUDGET = doublePreferencesKey("monthly_budget")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val MONTHLY_SUMMARY_ENABLED = booleanPreferencesKey("monthly_summary_enabled")
    }

    val isLoggedIn: Flow<Boolean> = context.userPreferencesDataStore.data
        .catchPreferencesErrors()
        .map { preferences -> preferences[Keys.IS_LOGGED_IN] ?: false }

    val currentUser: Flow<User> = context.userPreferencesDataStore.data
        .catchPreferencesErrors()
        .map { preferences ->
            User(
                name = preferences[Keys.USER_NAME] ?: "Martin Perez",
                email = preferences[Keys.USER_EMAIL] ?: "martin.perez@email.com",
                city = preferences[Keys.USER_CITY] ?: "Cordoba",
                monthlyBudget = preferences[Keys.MONTHLY_BUDGET] ?: 265000.0
            )
        }

    val notificationsEnabled: Flow<Boolean> = context.userPreferencesDataStore.data
        .catchPreferencesErrors()
        .map { preferences -> preferences[Keys.NOTIFICATIONS_ENABLED] ?: true }

    val monthlySummaryEnabled: Flow<Boolean> = context.userPreferencesDataStore.data
        .catchPreferencesErrors()
        .map { preferences -> preferences[Keys.MONTHLY_SUMMARY_ENABLED] ?: true }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun saveUser(user: User) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.USER_NAME] = user.name
            preferences[Keys.USER_EMAIL] = user.email
            preferences[Keys.USER_CITY] = user.city
            preferences[Keys.MONTHLY_BUDGET] = user.monthlyBudget
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setMonthlySummaryEnabled(enabled: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[Keys.MONTHLY_SUMMARY_ENABLED] = enabled
        }
    }

    private fun Flow<Preferences>.catchPreferencesErrors(): Flow<Preferences> =
        catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
}
