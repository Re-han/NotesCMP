package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.viewmodels.HomeVM
import data.viewmodels.NoteDetailsVM
import domain.Note
import domain.NoteEvents
import domain.Results
import epochToNormalTime

class HomePageNotes : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var searchValue by remember { mutableStateOf("") }
        var gridListView by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        val sortList = remember {
            mutableListOf(
                "Sort by Ascending",
                "Sort by Descending",
                "Sort by Date (latest)",
                "Sort by Date (oldest)",
            )
        }
        val homeVM = getScreenModel<HomeVM>()
        val noteDetailsVM = getScreenModel<NoteDetailsVM>()
        val pinnedNotes by homeVM.pinnedNotes
        val otherNotes by homeVM.otherNotes

        if (showDialog)
            Dialog(onDismissRequest = {
                showDialog = false
            }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    LazyColumn {
                        items(sortList.size) {
                            Column(modifier = Modifier
                                .clickable { }) {
                                Text(
                                    text = sortList[it],
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                        .wrapContentSize(Alignment.Center),
                                    textAlign = TextAlign.Center,
                                )
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("Notes")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.push(NoteDetailScreen(null, true)) },
                    shape = RoundedCornerShape(size = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Edit Icon"
                    )
                }
            }
        ) { paddingValues ->
            Column(
                Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
                    .padding(paddingValues).padding(8.dp)
            ) {
                TextField(
                    value = searchValue,
                    onValueChange = {
                        searchValue = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    placeholder = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, "search")
                    },
                    trailingIcon = {
                        if (gridListView)
                            Icon(
                                Icons.Outlined.GridView,
                                "grid",
                                Modifier.clickable { gridListView = false })
                        else
                            Icon(
                                Icons.Outlined.Menu,
                                "list",
                                Modifier.clickable { gridListView = true })
                    },
                    modifier = Modifier.fillMaxWidth().shadow(
                        1.dp,
                        shape = RoundedCornerShape(20.dp),
                    ),
                    textStyle = TextStyle(Color.Black),
                    shape = RoundedCornerShape(20.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text("Sort By")
                    Icon(
                        Icons.AutoMirrored.Filled.Sort,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            showDialog = true
                        })
                }

                Column {
                    NotesUIAndOperations(
                        Modifier,
                        onSelect = {
                            navigator.push(NoteDetailScreen(it, false))
                        },
                        onDelete = {
                            noteDetailsVM.setEvents(NoteEvents.Delete(it))
                        },
                        pinned = { note, notePinned ->
                            noteDetailsVM.setEvents(NoteEvents.SetPinned(note, notePinned))
                        },
                        noteResults = pinnedNotes,
                        gridListView = gridListView,
                        sectionTitle = "Pinned Notes"
                    )
                }
                Column {
                    NotesUIAndOperations(
                        Modifier,
                        onSelect = {
                            navigator.push(NoteDetailScreen(it, false))
                        },
                        onDelete = {
                            noteDetailsVM.setEvents(NoteEvents.Delete(it))
                        },
                        pinned = { note, notePinned ->
                            noteDetailsVM.setEvents(NoteEvents.SetPinned(note, notePinned))
                        },
                        noteResults = otherNotes,
                        gridListView = gridListView,
                        sectionTitle = "Other Notes"
                    )
                }
                if (pinnedNotes.getSuccessDataOrNull()?.isEmpty() == true
                    && otherNotes.getSuccessDataOrNull()?.isEmpty() == true
                )
                    Text(
                        "No Notes",
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        textAlign = TextAlign.Center
                    )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesUIAndOperations(
    modifier: Modifier = Modifier,
    noteResults: Results<List<Note>>?,
    onSelect: ((Note) -> Unit)? = null,
    onDelete: ((Note) -> Unit)? = null,
    pinned: ((Note, Boolean) -> Unit)? = null,
    gridListView: Boolean,
    sectionTitle: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete: Note? by remember { mutableStateOf(null) }

    if (showDialog) {
        AlertDialog(
            title = {
                Text(text = "Delete", fontSize = MaterialTheme.typography.titleLarge.fontSize)
            },
            text = {
                Text(
                    text = "Are you sure you want to remove '${noteToDelete!!.title}' note?",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            },
            confirmButton = {
                Button(onClick = {
                    onDelete?.invoke(noteToDelete!!)
                    showDialog = false
                    noteToDelete = null
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        noteToDelete = null
                        showDialog = false
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            onDismissRequest = {
                noteToDelete = null
                showDialog = false
            }
        )
    }

    noteResults?.DisplayResult(
        onLoading = {
            Text("Loading")
        },
        onSuccess = { notes ->
            if (notes.isNotEmpty()) {
                Column() {
                    Text(
                        sectionTitle,
                        style = TextStyle(fontSize = MaterialTheme.typography.headlineLarge.fontSize),
                        modifier = modifier.padding(horizontal = 8.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (gridListView) {
                        LazyVerticalStaggeredGrid(
                            modifier = modifier.fillMaxHeight()
                                .padding(vertical = 16.dp, horizontal = 8.dp),
                            columns = StaggeredGridCells.Fixed(2),
                            verticalItemSpacing = 8.dp,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            content = {
                                items(notes, key = { note -> note._id.toHexString() }) { note ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth()
                                            .combinedClickable(onClick = {
                                                onSelect?.invoke(note)
                                            }, onLongClick = {
                                                showDialog = true
                                                noteToDelete = note
                                            }),
                                        border = BorderStroke(
                                            .5.dp,
                                            color = MaterialTheme.colorScheme.tertiaryContainer
                                        ),
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth(.8f),
                                                horizontalAlignment = Alignment.Start
                                            ) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    note.title,
                                                    style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                                                    overflow = TextOverflow.Ellipsis
                                                ) //title

                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    note.description,
                                                    style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                                                    maxLines = 3,
                                                    overflow = TextOverflow.Ellipsis
                                                ) //description

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    epochToNormalTime(
                                                        note?.createdAt ?: 0L
                                                    ),
                                                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                ) //time
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                            if (note.pinned)
                                                Icon(
                                                    Icons.Default.PushPin,
                                                    contentDescription = null,
                                                    modifier = Modifier.padding(vertical = 8.dp)
                                                        .clickable { pinned?.invoke(note, false) })
                                            else
                                                Icon(
                                                    Icons.Outlined.PushPin,
                                                    contentDescription = null,
                                                    modifier.padding(vertical = 8.dp).clickable {
                                                        pinned?.invoke(
                                                            note, true
                                                        )
                                                    })
                                        }
                                    }
                                }
                            },
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                                .fillMaxWidth()
                        ) {
                            items(notes, key = { note -> note._id.toHexString() }) { note ->
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                        .combinedClickable(onClick = {
                                            onSelect?.invoke(note)
                                        }, onLongClick = {
                                            showDialog = true
                                            noteToDelete = note
                                        }),
                                    border = BorderStroke(
                                        .5.dp,
                                        color = MaterialTheme.colorScheme.tertiaryContainer
                                    ),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column() {
                                            Text(
                                                note.title,
                                                overflow = TextOverflow.Ellipsis,
                                                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                                            ) //title
                                            Text(
                                                note.description,
                                                overflow = TextOverflow.Ellipsis,
                                                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                                                maxLines = 3
                                            ) //description
                                            Text(
                                                epochToNormalTime(note?.createdAt ?: 0L),
                                                overflow = TextOverflow.Ellipsis,
                                                style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                                                maxLines = 1
                                            ) //time
                                        }
                                        if (note.pinned)
                                            Icon(
                                                Icons.Default.PushPin,
                                                contentDescription = null,
                                                modifier.clickable { pinned?.invoke(note, false) })
                                        else
                                            Icon(
                                                Icons.Outlined.PushPin,
                                                contentDescription = null,
                                                modifier.clickable {
                                                    pinned?.invoke(
                                                        note, true
                                                    )
                                                })
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        },
        onError = {
            Text("SomeThing went wrong")
        },
    )

}