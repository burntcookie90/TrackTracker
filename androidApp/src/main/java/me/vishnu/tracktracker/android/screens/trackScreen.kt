package me.vishnu.tracktracker.android.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import me.vishnu.tracktracker.shared.stores.Loop
import me.vishnu.tracktracker.shared.stores.tracks.*

@Composable
fun TrackScreen(store: TrackScreenStore, effectHandler: TrackScreenEffectHandler) {
  val (loopState, dispatch) = remember {
    Loop(
      store = store,
      effectHandler = effectHandler,
      startEffects = { setOf(TrackScreenEffects.LoadInitialData) },
    )
  }

  TrackScreen(model = loopState.value, dispatch = dispatch)
}

@Composable
fun TrackScreen(model: TrackScreenModel, dispatch: (TrackScreenEvents) -> Unit) {
  Text(text = "Hello from track screen")
}