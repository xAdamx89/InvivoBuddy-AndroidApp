package pl.amfm.invivobuddy.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.amfm.invivobuddy.data.repository.AuthRepository

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    // LiveData do informowania Activity o stanie operacji
    private val _registrationResult = MutableLiveData<Result<String>>()
    val registrationResult: LiveData<Result<String>> = _registrationResult

    // LiveData do pokazywania kółka ładowania (ProgressBar)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(login: String, pass: String) {
        // Uruchamiamy operację w tle (Coroutine)
        viewModelScope.launch {
            _isLoading.value = true

            try {
                // Wywołujemy funkcję z Twojego Repozytorium
                val response = repository.register(login, pass)

                if (response.isSuccessful) {
                    _registrationResult.value = Result.success("Konto utworzone pomyślnie!")
                } else {
                    // Wyciągamy treść błędu z serwera
                    val errorJson = response.errorBody()?.string()

                    // Opcjonalnie: możesz tu użyć biblioteki Gson, aby wyciągnąć samo pole "detail" z FastAPI
                    // Na razie przekażemy surowy tekst błędu
                    val errorMessage = errorJson ?: "Nieznany błąd serwera"

                    _registrationResult.value = Result.failure(
                        Exception("Błąd ${response.code()}: $errorMessage"))
                }
            } catch (e: Exception) {
                _registrationResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}