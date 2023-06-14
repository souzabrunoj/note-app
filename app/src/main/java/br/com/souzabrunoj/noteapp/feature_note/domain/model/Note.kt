package br.com.souzabrunoj.noteapp.feature_note.domain.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.souzabrunoj.noteapp.ui.theme.BabyBlue
import br.com.souzabrunoj.noteapp.ui.theme.RedOrange
import br.com.souzabrunoj.noteapp.ui.theme.RedPink
import br.com.souzabrunoj.noteapp.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
){
    companion object{
        val noteColors = listOf(RedOrange, Color.LightGray, Violet, BabyBlue, RedPink)
    }
}
class InvalidNoteException(message: String): Exception(message)
