package data.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.Note
import domain.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeVM(private var mongoDB: MongoDB) : ScreenModel {
    private var _pinnedNote: MutableState<Results<List<Note>>> = mutableStateOf(Results.Loading)
    val pinnedNotes = _pinnedNote

    private var _otherNote: MutableState<Results<List<Note>>> = mutableStateOf(Results.Loading)
    val otherNotes = _otherNote

    init {
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.readPinnedNotes().collectLatest {
                _pinnedNote.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.readAllNotes().collectLatest {
                _otherNote.value = it
            }
        }
    }
}