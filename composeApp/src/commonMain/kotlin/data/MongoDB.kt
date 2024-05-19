package data

import domain.Note
import domain.Results
import domain.SortOrder
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MongoDB {
    var realm: Realm? = null

    init {
        configureRealmDB()
    }

    private fun configureRealmDB() {
        if (realm == null || realm!!.isClosed()) {
            val realmConfig = RealmConfiguration.Builder(
                schema = setOf(Note::class)
            ).compactOnLaunch().build()
            realm = Realm.open(realmConfig)
        }
    }

    fun readOtherNotes(sortOrder: SortOrder = SortOrder.Sort_By_Date_Latest): Flow<Results<List<Note>>> {
        return realm?.query<Note>(query = "createdAt != $0", 0L)?.asFlow()?.map { res ->
            Results.Success(data =
            when (sortOrder) {
                SortOrder.Sort_By_Desc -> {
                    res.list.sortedByDescending { notes -> notes.title }
                }

                SortOrder.Sort_By_Asc -> {
                    res.list.sortedBy { note: Note -> note.title }
                }

                SortOrder.Sort_By_Date_Latest -> {
                    res.list.sortedByDescending { note: Note -> note.createdAt }
                }

                else -> if (sortOrder == SortOrder.Sort_By_Date_Oldest) {
                    res.list.sortedBy { note: Note -> note.createdAt }
                } else {
                    res.list.sortedByDescending { note: Note -> note.createdAt }
                }.filter { note: Note -> !note.pinned }
            })
        } ?: flow { Results.Error("No Notes") }
    }

    fun readPinnedNotes(sortOrder: SortOrder = SortOrder.Sort_By_Date_Latest): Flow<Results<List<Note>>> {
        return realm?.query<Note>(query = "pinned == $0", true)?.asFlow()?.map { res ->
            Results.Success(data = when (sortOrder) {
                SortOrder.Sort_By_Desc -> {
                    res.list.sortedByDescending { notes -> notes.title }
                }

                SortOrder.Sort_By_Asc -> {
                    res.list.sortedBy { note: Note -> note.title }
                }

                SortOrder.Sort_By_Date_Latest -> {
                    res.list.sortedByDescending { note: Note -> note.createdAt }
                }

                else -> if (sortOrder == SortOrder.Sort_By_Date_Oldest) {
                    res.list.sortedBy { note: Note -> note.createdAt }
                } else {
                    res.list.sortedByDescending { note: Note -> note.createdAt }
                }.filter { note: Note -> note.pinned }
            })
        } ?: flow { Results.Error("No Pinned Notes") }
    }

    suspend fun addNote(note: Note) {
        realm?.write { copyToRealm(note) }
    }

    suspend fun updateNote(note: Note) {
        realm?.write {
            try {
                val existingNote = realm?.query<Note>("_id == $0", note._id)?.find()?.first()
                existingNote?.let {
                    findLatest(it)?.let { originalNote ->
                        originalNote.title = note.title
                        originalNote.description = note.description
                        originalNote.createdAt = note.createdAt
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    suspend fun updateNotePinned(updatedNote: Note, pinned: Boolean) {
        realm?.write {
            try {
                val existingNote = realm?.query<Note>("_id == $0", updatedNote._id)?.find()?.first()
                existingNote?.let {
                    findLatest(it)?.let { note ->
                        note.pinned = pinned
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    suspend fun deleteNote(note: Note) {
        realm?.write {
            try {
                val existingNote = realm?.query<Note>("_id == $0", note._id)?.find()?.first()
                existingNote?.let {
                    findLatest(it)?.let { note ->
                        delete(note)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
}