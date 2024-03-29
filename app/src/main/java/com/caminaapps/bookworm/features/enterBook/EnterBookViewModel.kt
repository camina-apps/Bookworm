package com.caminaapps.bookworm.features.enterBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caminaapps.bookworm.core.model.Book
import com.caminaapps.bookworm.features.enterBook.EnterBookUiState.BookSaved
import com.caminaapps.bookworm.features.enterBook.EnterBookUiState.EnterBookInfo
import com.caminaapps.bookworm.features.enterBook.EnterBookUiState.ErrorTitleMissing
import com.caminaapps.bookworm.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EnterBookViewModel @Inject constructor(
    private val saveBookUseCase: SaveEnteredBookUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow<EnterBookUiState>(EnterBookInfo)
    val uiState: StateFlow<EnterBookUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = EnterBookInfo
    )

    fun saveBook(book: Book) {
        viewModelScope.launch {
            try {
                saveBookUseCase(book)
                _uiState.value = BookSaved
            } catch (e: BookTitleMissing) {
                Timber.d(e)
                _uiState.value = ErrorTitleMissing
            }
        }
    }

    fun titleInputChanged() {
        _uiState.value = EnterBookInfo
    }
}

sealed interface EnterBookUiState {
    object EnterBookInfo : EnterBookUiState
    object BookSaved : EnterBookUiState
    object ErrorTitleMissing : EnterBookUiState
}
