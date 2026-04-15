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
import pl.amfm.invivobuddy.data.remote.RetrofitClient
import pl.amfm.invivobuddy.data.repository.AuthRepository
import pl.amfm.invivobuddy.databinding.ActivityLoginBinding
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
                // SUKCES! Na razie tylko przechodzimy dalej
                Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show()

                // TU DOCELOWO: Zapisz tokenData.access_token w SharedPreferences

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Zamykamy logowanie, by nie wrócić do niego przyciskiem "wstecz"
            }

            result.onFailure { exception ->
                android.util.Log.e("API_ERROR", "Treść błędu: ${exception.message}")
                Snackbar.make(binding.root, "Błąd walidacji danych!", Snackbar.LENGTH_LONG).show()
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