package br.com.souzabrunoj.noteapp.feature_note.presentation.add_edit_notes

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
    object SaveNote : UiEvent()
}
