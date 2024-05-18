package data.viewmodels

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.NoteEvents
import domain.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class NoteDetailsVM(private var mongoDB: MongoDB) : ScreenModel {
    fun setEvents(event: NoteEvents) {
        when (event) {
            is NoteEvents.Add -> {
                addNote(event.note)
            }

            is NoteEvents.Delete -> {
                deleteNote(event.note)
            }

            is NoteEvents.SetPinned -> {
                updateNotePinned(event.note, event.pinned)
            }

            is NoteEvents.Update -> {
                updateNote(event.note)
            }
        }
    }

    private fun addNote(note: Note) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.addNote(note)
        }
    }

    private fun deleteNote(note: Note) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.deleteNote(note)
        }
    }

    private fun updateNote(note: Note) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.updateNote(note)
        }
    }

    private fun updateNotePinned(note: Note, pinned: Boolean) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.updateNotePinned(note, pinned)
        }
    }
}