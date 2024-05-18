package domain

sealed class NoteEvents {
    data class Add(val note: Note) : NoteEvents()
    data class Update(val note: Note) : NoteEvents()
    data class Delete(val note: Note) : NoteEvents()
    data class SetPinned(val note: Note, val pinned: Boolean) : NoteEvents()
}