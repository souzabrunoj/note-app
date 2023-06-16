package br.com.souzabrunoj.noteapp.feature_note.domain.use_case

import br.com.souzabrunoj.noteapp.feature_note.domain.model.InvalidNoteException
import br.com.souzabrunoj.noteapp.feature_note.domain.model.Note
import br.com.souzabrunoj.noteapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(private val repository: NoteRepository) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        when {
            note.title.isBlank() -> throw InvalidNoteException("The title of note can't be empty.")
            note.content.isBlank() -> throw InvalidNoteException("The content of note can't be empty.")
            else -> repository.insertNote(note)
        }
    }
}