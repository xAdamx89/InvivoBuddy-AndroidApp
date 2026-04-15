package pl.amfm.invivobuddy.ui.login

import LoginViewModel
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import pl.amfm.invivobuddy.MainActivity
import pl.amfm.invivobuddy.data.local.SessionManager
import pl.amfm.invivobuddy.data.remote.RetrofitClient
import pl.amfm.invivobuddy.data.repository.AuthRepository
import pl.amfm.invivobuddy.databinding.ActivityLoginBinding
import pl.amfm.invivobuddy.ui.SecondActivity
import pl.amfm.invivobuddy.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    // Używamy nowoczesnego ViewBindingu, żeby nie szukać po ID!
    private lateinit var binding: ActivityLoginBinding

    // 5. INICJALIZACJA VIEWMODELU
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(AuthRepository(RetrofitClient.apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. INICJALIZACJA SPLASH SCREENA (Musi być na samej górze!)
        installSplashScreen()

        // SPRAWDZANIE SESJI
        val sessionManager = SessionManager(this)
        if (sessionManager.fetchAuthToken() != null) {
            // Token istnieje -> idziemy prosto do aplikacji
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
            return // Ważne, żeby nie ładować reszty tego Activity!
        }

        // 2. EDGE-TO-EDGE
        enableEdgeToEdge()

        // Dopiero teraz wywołujemy standardowy kod Androida
        super.onCreate(savedInstanceState)

        // 3. ŁADOWANIE WIDOKU PRZEZ VIEWBINDING
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 4. BEZPIECZNE MARGINESY (używamy binding.root zamiast szukania R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { tokenData ->
                // 1. Zapisujemy token
                val sessionManager = SessionManager(this)
                sessionManager.saveAuthToken(tokenData.access_token)

                // 2. Informujemy użytkownika
                Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show()

                // 3. Przechodzimy do SecondActivity
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)

                // 4. Kończymy LoginActivity
                finish()
            }

            result.onFailure { exception ->
                android.util.Log.e("API_ERROR", "Treść błędu: ${exception.message}")
                // Wyświetlamy realny błąd z serwera zamiast sztywnego tekstu
                val errorMsg = exception.message ?: "Błąd logowania"
                Snackbar.make(binding.root, errorMsg, Snackbar.LENGTH_LONG).show()
            }
        }

        // 5. OBSŁUGA PRZYCISKÓW

        // KLIKNIĘCIE ZALOGUJ
        binding.loginButton.setOnClickListener {
            val username = binding.userNameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(username, password)
            } else {
                Toast.makeText(this, "Wpisz dane logowania", Toast.LENGTH_SHORT).show()
            }
        }

        // Przejście do ekranu rejestracji
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Blokujemy przycisk, żeby nie klikać wielokrotnie
            binding.loginButton.isEnabled = !isLoading
        }
    }
}