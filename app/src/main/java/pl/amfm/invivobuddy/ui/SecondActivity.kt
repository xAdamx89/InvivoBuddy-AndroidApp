package pl.amfm.invivobuddy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.amfm.invivobuddy.databinding.ActivitySecondBinding
import com.google.android.material.snackbar.Snackbar
import pl.amfm.invivobuddy.data.local.SessionManager
import pl.amfm.invivobuddy.ui.login.LoginActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val sessionManager by lazy { SessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.clickMeButton.setOnClickListener { view ->
            Snackbar.make(view, "Cześć!", Snackbar.LENGTH_SHORT).show()
        }

        // --- OBSŁUGA WYLOGOWANIA ---
        binding.logOutButton.setOnClickListener {
            // 1. Usuwamy token z pamięci
            sessionManager.clearSession()

            // 2. Informujemy użytkownika
            Toast.makeText(this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show()

            // 3. Powrót do ekranu logowania
            val intent = Intent(this, LoginActivity::class.java)

            // Czyścimy flagi, żeby użytkownik nie mógł wrócić "wstecz" do SecondActivity
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()
        }
    }
}
