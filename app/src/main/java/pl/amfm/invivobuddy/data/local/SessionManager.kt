package pl.amfm.invivobuddy.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SessionManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        "secure_user_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_TOKEN = "auth_token"
    }

    // Zapisywanie tokena
    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    // Pobieranie tokena
    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    // Wylogowanie
    fun clearSession() {
        sharedPreferences.edit().remove(KEY_TOKEN).apply()
    }
}