package pl.amfm.invivobuddy.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // TO JEST TA KRYTYCZNA LINIJKA!
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.amfm.invivobuddy.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // Używamy nowoczesnego ViewBindingu, żeby nie szukać po ID!
    private lateinit var binding: ActivityLoginBinding

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

        // 5. OBSŁUGA PRZYCISKÓW
        // Przejście do ekranu rejestracji
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Tutaj docelowo dodamy logikę logowania (kliknięcie przycisku, itp.)
    }
}