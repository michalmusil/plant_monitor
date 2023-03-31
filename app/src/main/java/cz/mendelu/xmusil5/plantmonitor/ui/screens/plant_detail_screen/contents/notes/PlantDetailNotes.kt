package cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.contents.notes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.plantmonitor.R
import cz.mendelu.xmusil5.plantmonitor.models.api.plant_note.GetPlantNote
import cz.mendelu.xmusil5.plantmonitor.navigation.INavigationRouter
import cz.mendelu.xmusil5.plantmonitor.ui.components.list_items.PlantNoteListItem
import cz.mendelu.xmusil5.plantmonitor.ui.components.screens.NoPlantNotes
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.plantmonitor.ui.components.ui_elements.CustomTextField
import cz.mendelu.xmusil5.plantmonitor.ui.screens.plant_detail_screen.PlantDetailViewModel
import cz.mendelu.xmusil5.plantmonitor.ui.utils.Edges
import cz.mendelu.xmusil5.plantmonitor.ui.utils.UiConstants
import cz.mendelu.xmusil5.plantmonitor.utils.fadeEdges
import kotlinx.coroutines.launch

@Composable
fun PlantDetailNotes(
    viewModel: PlantDetailViewModel,
    navigation: INavigationRouter
){
    val coroutineScope = rememberCoroutineScope()

    val notes = viewModel.plantNotes.collectAsState()

    val notesToDisplay = remember{
        mutableStateListOf<GetPlantNote>()
    }

    val listState = rememberLazyListState()

    LaunchedEffect(notes.value){
        notes.value?.let {
            val reorderedNotes = viewModel.getPlantNotesOrderedForDisplay(notes.value!!)

            val oldFirstNoteDate = notesToDisplay.firstOrNull()?.created?.calendarInUTC0?.timeInMillis
            val newFirstNoteDate = reorderedNotes.firstOrNull()?.created?.calendarInUTC0?.timeInMillis

            notesToDisplay.clear()
            notesToDisplay.addAll(reorderedNotes)

            // If a new note gets added, i scroll the list to the top
            if (oldFirstNoteDate != null && newFirstNoteDate != null
                && oldFirstNoteDate != newFirstNoteDate){
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
    ) {

        AddNewNoteField(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        if (notesToDisplay.isEmpty()){
            NoPlantNotes(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f)
                    .fadeEdges(
                        edges = Edges.VERTICAL,
                        backgroundColor = MaterialTheme.colorScheme.background,
                        fadeWidth = UiConstants.EDGE_FADE_MEDIUM
                    )
            ) {
                items(
                    count = notesToDisplay.count(),
                    key = {
                        notesToDisplay[it].id
                    },
                    itemContent = { index ->
                        val note = notesToDisplay[index]

                        PlantNoteListItem(
                            note = note,
                            onDelete = {
                                viewModel.deletePlantNote(it.id)
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AddNewNoteField(
    viewModel: PlantDetailViewModel,
    modifier: Modifier
){
    val plant = viewModel.plant.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ){
        val addButtonEnabled = rememberSaveable{
            mutableStateOf(false)
        }
        val newNoteContent = rememberSaveable{
            mutableStateOf("")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(3f)
            ) {
                CustomTextField(
                    labelTitle = stringResource(id = R.string.newNote),
                    value = newNoteContent,
                    singleLine = false,
                    maxChars = 1000,
                    onTextChanged = {
                        addButtonEnabled.value = it.isNotBlank()
                    },
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                CustomButton(
                    text = "",
                    iconId = R.drawable.ic_plus,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textSize = 25.sp,
                    enabled = addButtonEnabled.value,
                    onClick = {
                        plant.value?.let {
                            viewModel.addPlantNote(
                                text = newNoteContent.value,
                                plantId = it.id
                            )
                            addButtonEnabled.value = false
                            newNoteContent.value = ""
                        }
                    },
                )
            }
        }
    }
}