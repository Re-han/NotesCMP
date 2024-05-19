package data.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.Note
import domain.Results
import domain.SortOrder
import domain.SortOrder.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeVM(private var mongoDB: MongoDB) : ScreenModel {

    var sortOrder: SortOrder = Sort_By_Date_Latest
    fun sorting(sortOrder: SortOrder) {
        when (sortOrder) {
            Sort_By_Asc -> {
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readPinnedNotes(Sort_By_Asc).collectLatest {
                        _pinnedNote.value = it
                    }
                }
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readOtherNotes(Sort_By_Asc).collectLatest {
                        _otherNote.value = it
                    }
                }
            }

            Sort_By_Date_Latest -> {
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readPinnedNotes(Sort_By_Date_Latest).collectLatest {
                        _pinnedNote.value = it
                    }
                }
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readOtherNotes(Sort_By_Date_Latest).collectLatest {
                        _otherNote.value = it
                    }
                }
            }

            Sort_By_Date_Oldest -> {
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readPinnedNotes(Sort_By_Date_Oldest).collectLatest {
                        _pinnedNote.value = it
                    }
                }
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readOtherNotes(Sort_By_Date_Oldest).collectLatest {
                        _otherNote.value = it
                    }
                }
            }

            Sort_By_Desc -> {
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readPinnedNotes(Sort_By_Desc).collectLatest {
                        _pinnedNote.value = it
                    }
                }
                screenModelScope.launch(Dispatchers.Main) {
                    mongoDB.readOtherNotes(Sort_By_Desc).collectLatest {
                        _otherNote.value = it
                    }
                }
            }
        }
    }

    private var _pinnedNote: MutableState<Results<List<Note>>> = mutableStateOf(Results.Loading)
    val pinnedNotes = _pinnedNote

    private var _otherNote: MutableState<Results<List<Note>>> = mutableStateOf(Results.Loading)
    val otherNotes = _otherNote

    init {
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.readPinnedNotes(sortOrder).collectLatest {
                _pinnedNote.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.readOtherNotes(sortOrder).collectLatest {
                _otherNote.value = it
            }
        }
    }
}