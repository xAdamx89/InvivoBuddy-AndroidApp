package pl.amfm.invivobuddy.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Model danych wysyłanych do FastAPI (musi pasować do UserCreate w Pythonie)
data class RegisterRequest(
    val username: String,
    val email: String? = null,
    val password: String,
    val avatar_url: String? = null
)

data class UserLoginRequest(
    val username: String,
    val password: String
)

data class UserResponse(
    val id: Int,
    val username: String,
    val email: String? = null,
    val created_at: String,
    val avatar_url: String? = null
)

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val token_type: String = "bearer"
)

interface ApiService {
    @POST("/register")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<UserResponse>

    @POST("/login")
    suspend fun loginUser(
        @Body request: UserLoginRequest
    ): Response<TokenResponse>
}