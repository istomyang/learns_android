package ty.learns.android.ui.first

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FirstViewModelFactory(private val inputText: String, private val tapId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FirstViewModel(inputText, tapId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}