package pl.amfm.invivobuddy.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.amfm.invivobuddy.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

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

        // POWRÓT DO LOGOWANIA
        binding.backToLoginButton.setOnClickListener {
            // finish() po prostu zamyka to okno i "odsłania" LoginActivity, które jest pod spodem
            finish()
        }

        // PRZYCISK ZAREJESTRUJ
        binding.registerButton.setOnClickListener {
            finish()
        }
    }
}