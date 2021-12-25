package me.vishnu.tracktracker.android.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.vishnu.tracktracker.shared.stores.Loop
import me.vishnu.tracktracker.shared.stores.tracks.*
import me.vishnu.tracktracker.shared.stores.welcome.CarScreenEvents

@Composable
fun TrackScreen(store: TrackScreenStore, effectHandler: TrackScreenEffectHandler) {
  val (loopState, dispatch) = remember {
    Loop(
      store = store,
      effectHandler = effectHandler,
      startEffects = { setOf(TrackScreenEffects.LoadInitialData) },
    )
  }

  TrackScreen(
    model = loopState.collectAsState().value,
    dispatch = dispatch
  )
}

@Composable
fun TrackScreen(model: TrackScreenModel, dispatch: (TrackScreenEvents) -> Unit) {
  Scaffold(
    topBar = { TopAppBar(title = { Text("Track Tracker") }) },
    floatingActionButton = {
      if (!model.addTrackModel) {
        FloatingActionButton(onClick = {
          dispatch(TrackScreenEvents.AddTrack)
        }) { Icon(Icons.Default.Add, contentDescription = "Add Track Button") }
      }
    }
  ) {
    if (model.addTrackModel) {
      TrackScreenAddTrack(dispatch)
    } else {
      TrackScreenDisplay(model)
    }
  }
}

@Composable
fun TrackScreenDisplay(model: TrackScreenModel) {

}

@Composable
fun TrackScreenAddTrack(dispatch: (TrackScreenEvents) -> Unit) {
  val name = remember { mutableStateOf("") }
  val location = remember { mutableStateOf("") }

  Column(
    modifier = Modifier.fillMaxWidth()
      .padding(16.dp)
  ) {

    TextField(
      modifier = Modifier.fillMaxWidth(),
      value = name.value,
      onValueChange = { name.value = it },
      label = {
        Text("Track Name")
      }
    )

    TextField(
      modifier = Modifier.fillMaxWidth(),
      value = location.value,
      onValueChange = { location.value = it },
      label = {
        Text("Location")
      }
    )

    Row {
      Button(onClick = { dispatch(TrackScreenEvents.DismissAddTrack) }) {
        Text("Cancel")
      }

      Button(onClick = {
        dispatch(
          TrackScreenEvents.CreateTrack(
            name = name.value,
            location = location.value
          )
        )
      }) {
        Text("Submit")
      }
    }
  }
}