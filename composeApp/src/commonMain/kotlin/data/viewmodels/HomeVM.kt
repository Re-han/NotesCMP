package data.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.Notes
import domain.Results
import domain.Results.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeVM(private var mongoDB: MongoDB) : ScreenModel {
    private var _pinnedNotes: MutableState<Results<List<Notes>>> = mutableStateOf(Results.Loading)
    val pinnedNotes = _pinnedNotes

    private var _otherNotes: MutableState<Results<List<Notes>>> = mutableStateOf(Results.Loading)
    val otherNotes = _otherNotes

    init {
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.readPinnedNotes().collectLatest {
                _pinnedNotes.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.readAllNotes().collectLatest {
                _otherNotes.value = it
            }
        }
    }


}