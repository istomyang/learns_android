package ty.learns.android.ui.first

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.*

class FirstViewModel(inputText: String, tapId: Int) : ViewModel(), Observable {

    private val _userInputText = MutableLiveData<String>()
    private val userInputText: LiveData<String> get() = _userInputText

    val userInputText2 = MutableLiveData<String>()

    private val _userTapId = MutableLiveData<Int>()
    val userTapId: LiveData<Int> get() = _userTapId

    init {
        Log.i("FirstViewModel", "FirstViewModel created!")

        _userInputText.value = inputText
        _userTapId.value = tapId
        userInputText2.value = inputText
    }

//    fun getUserInputText(): String? = _userInputText.value
//
//    fun setUserInputText(value: String) {
//        if (value != _userInputText.value) {
//            _userInputText.value = value
//        }
//    }

    fun updateInputText(newInputText: String) {
        _userInputText.value = newInputText
    }

    fun updateTapId(id: Int) {
        _userTapId.value = id
    }

    // 可以参考 BaseObservable
    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback) {
        callbacks.remove(callback)
    }

    /**
     * Notifies observers that all properties of this instance have changed.
     */
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies observers that a specific property has changed. The getter for the
     * property that changes should be marked with the @Bindable annotation to
     * generate a field in the BR class to be used as the fieldId parameter.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }

    // 在对应依赖的试图全部退出后退出。
    override fun onCleared() {
        Log.i("FirstViewModel", "FirstViewModel cleared!")
        super.onCleared()
    }
}