package br.com.souzabrunoj.noteapp.di

import android.app.Application
import androidx.room.Room
import br.com.souzabrunoj.noteapp.feature_note.data.data_source.NoteDatabase
import br.com.souzabrunoj.noteapp.feature_note.data.data_source.NoteDatabase.Companion.DATABASE_NAME
import br.com.souzabrunoj.noteapp.feature_note.data.repository.NoteRepositoryImpl
import br.com.souzabrunoj.noteapp.feature_note.domain.repository.NoteRepository
import br.com.souzabrunoj.noteapp.feature_note.domain.use_case.AddNoteUseCase
import br.com.souzabrunoj.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import br.com.souzabrunoj.noteapp.feature_note.domain.use_case.GetNoteUseCase
import br.com.souzabrunoj.noteapp.feature_note.domain.use_case.GetNotesUseCase
import br.com.souzabrunoj.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDataBase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = NoteDatabase::class.java,
            name = DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(database: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(database.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)
        )
    }
}