// AuthViewModel.kt
class AuthViewModel : ViewModel() {
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.login(username, password)
            if (response.isSuccessful) {
                // Save token securely
                securePrefs.saveToken(response.body()?.token)
                // Initialize Socket.IO connection
                socketManager.initialize(response.body()?.token)
            }
        }
    }
}

// SecurePrefs.kt (Encrypted SharedPreferences)
class SecurePrefs(context: Context) {
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String?) {
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()
    }
}