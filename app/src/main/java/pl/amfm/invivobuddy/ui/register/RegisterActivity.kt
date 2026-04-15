package pl.amfm.invivobuddy.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import pl.amfm.invivobuddy.data.remote.RetrofitClient
import pl.amfm.invivobuddy.data.repository.AuthRepository
import pl.amfm.invivobuddy.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    // 1. Deklaracja ViewModelu z fabryką (Musi być tutaj, nie wewnątrz funkcji!)
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(AuthRepository(RetrofitClient.apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Ustawienie obserwatorów (Wywołujemy to raz, przy starcie)
        setupObservers()

        // POWRÓT DO LOGOWANIA
        binding.backToLoginButton.setOnClickListener {
            finish()
        }

        // PRZYCISK ZAREJESTRUJ
        binding.registerButton.setOnClickListener {
            val username = binding.userNameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.registerUser(username, password)
            } else {
                Toast.makeText(this, "Uzupełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Oddzielna funkcja dla czytelności kodu
    private fun setupObservers() {
        // Obserwowanie wyniku rejestracji
        viewModel.registrationResult.observe(this) { result ->
            result.onSuccess { successMessage ->
                Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show()
                // Opóźnienie zamknięcia ekranu, by user zdążył przeczytać Toast
                binding.root.postDelayed({ finish() }, 1500)
            }

            result.onFailure { exception ->
                android.util.Log.e("API_ERROR", "Treść błędu: ${exception.message}")
                Snackbar.make(binding.root, "Błąd walidacji danych!", Snackbar.LENGTH_LONG).show()
            }
        }

        // Obserwowanie stanu ładowania (opcjonalnie, jeśli masz progressBar)
        viewModel.isLoading.observe(this) { isLoading ->
            binding.registerButton.isEnabled = !isLoading
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}