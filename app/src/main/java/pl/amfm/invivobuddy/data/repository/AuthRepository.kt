package pl.amfm.invivobuddy.data.repository

import pl.amfm.invivobuddy.data.remote.*
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {

    suspend fun register(username: String, pass: String): Response<UserResponse> {
        // Musimy podać wszystkie pola wymagane przez RegisterRequest
        val request = RegisterRequest(
            username = username,
            password = pass,
            email = null,      // Na razie null, jeśli nie masz pola w UI
            avatar_url = null
        )
        return apiService.registerUser(request)
    }

    // LOGOWANIE
    suspend fun login(username: String, pass: String): Response<TokenResponse> {
        // Tworzymy paczkę danych zgodnie ze schematem UserLoginRequest z Twojego ApiService
        val request = UserLoginRequest(
            username = username,
            password = pass
        )

        // Wykonujemy zapytanie do endpointu /login zdefiniowanego w ApiService
        return apiService.loginUser(request)
    }
}