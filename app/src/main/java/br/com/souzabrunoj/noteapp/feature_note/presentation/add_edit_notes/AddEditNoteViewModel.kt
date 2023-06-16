package br.com.souzabrunoj.noteapp.feature_note.presentation.add_edit_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.souzabrunoj.noteapp.feature_note.domain.model.InvalidNoteException
import br.com.souzabrunoj.noteapp.feature_note.domain.model.Note
import br.com.souzabrunoj.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val useCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter some content..."))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFLow = MutableSharedFlow<UiEvent>()
    val eventFLow = _eventFLow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId").takeIf { it != -1 }?.let { noteId ->
            viewModelScope.launch {
                useCases.getNoteUseCase.invoke(noteId)?.also { note ->
                    currentNoteId = note.id
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        isHintVisible = false
                    )
                    _noteContent.value = noteContent.value.copy(
                        text = note.content,
                        isHintVisible = false
                    )
                    _noteColor.intValue = note.color
                }
            }
        }

    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.value)
            }

            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value =
                    noteTitle.value.copy(
                        isHintVisible = !event.focus.isFocused && noteTitle.value.text.isBlank()
                    )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }

            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value =
                    noteContent.value.copy(
                        isHintVisible = !event.focus.isFocused && noteContent.value.text.isBlank()
                    )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.intValue = event.color
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        useCases.addNoteUseCase.invoke(
                            Note(
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = _noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFLow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFLow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Could not save note"
                            )
                        )
                    }
                }
            }
        }
    }
}