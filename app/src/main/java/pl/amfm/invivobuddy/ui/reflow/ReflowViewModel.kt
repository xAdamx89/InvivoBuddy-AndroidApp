package pl.amfm.invivobuddy.ui.reflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReflowViewModel : ViewModel() {
    val text: LiveData<String> = _text
}


private val _text = MutableLiveData<String>().apply {
    value = "This is reflow Fragment"
}
