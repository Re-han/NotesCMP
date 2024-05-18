package data

import domain.Notes
import domain.Results
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
                schema = setOf(Notes::class)
            ).compactOnLaunch().build()
            realm = Realm.open(realmConfig)
        }
    }

    fun readAllNotes(): Flow<Results<List<Notes>>> {
        return realm?.query<Notes>(query = "createdAt != $0", 0L)?.asFlow()?.map { res ->
            Results.Success(data = res.list.sortedByDescending { notes -> notes.createdAt })
        } ?: flow { Results.Error("No Notes") }
    }

    fun readPinnedNotes(): Flow<Results<List<Notes>>> {
        return realm?.query<Notes>(query = "pinned == $0", true)?.asFlow()?.map { res ->
            Results.Success(data = res.list.sortedByDescending { notes -> notes.createdAt })
        } ?: flow { Results.Error("No Pinned Notes") }
    }

    suspend fun addNote(notes: Notes) {
        realm?.write { copyToRealm(notes) }
    }

    suspend fun updateNote(id: Int, notes: Notes) {
        val existingNote = realm?.query<Notes>("_id == $0", id)?.find()?.first()
        realm?.write {
            existingNote?.let {
                findLatest(it)?.let { note ->
                    note.title = notes.title
                    note.description = notes.description
                    note.pinned = notes.pinned
                    note.createdAt = notes.createdAt
                }
            }
        }
    }
}