package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.viewmodels.NoteDetailsVM
import domain.Note
import domain.NoteEvents
import epochToNormalTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NoteDetailScreen(private val note: Note? = null, private val Add: Boolean) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val noteDetailsVM = getScreenModel<NoteDetailsVM>()
        var titleValue by remember { mutableStateOf(note?.title ?: "") }
        var descriptionValue by remember { mutableStateOf(note?.description ?: "") }
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 16.dp).clickable {
                                if (Add && titleValue.isNotBlank()) {
                                    val note = Note().apply {
                                        title = titleValue
                                        description = descriptionValue
                                        pinned = false
                                        createdAt = Clock.System.now().epochSeconds
                                    }
                                    noteDetailsVM.setEvents(NoteEvents.Add(note))
                                } else {
                                    if (titleValue.isNotBlank()) {
                                        val note = Note().apply {
                                            _id = note?._id!!
                                            title = titleValue
                                            description = descriptionValue
                                            pinned = false
                                            createdAt = Clock.System.now().epochSeconds
                                        }
                                        noteDetailsVM.setEvents(NoteEvents.Update(note))
                                    }
                                }
                                navigator.pop()
                            })
                    },
                    title = {
                        TextField(
                            placeholder = {
                                Text(
                                    text = "Title", style =
                                    TextStyle(
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                                    )
                                )
                            },
                            value = titleValue,
                            onValueChange = {
                                titleValue = it
                            },
                            textStyle = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            ),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = MaterialTheme.colorScheme.surfaceContainer,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                )
            },
        ) { paddingValues ->
            Column(
                Modifier.fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
                    .padding(paddingValues)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    TextField(
                        placeholder = {
                            Text(
                                text = "Description", style =
                                TextStyle(
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                            )
                        },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        value = descriptionValue, onValueChange = {
                            descriptionValue = it
                        },
                        textStyle = TextStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            disabledTextColor = MaterialTheme.colorScheme.surfaceContainer,
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                            disabledIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(.8f)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                    )

                    if (!Add)
                        Box(
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .fillMaxWidth().fillMaxHeight(.3f)
                        ) {
                            Text(
                                "Edited on ${epochToNormalTime(note?.createdAt ?: 0L)}",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                ),
                                color = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                }
            }
        }
    }
}
