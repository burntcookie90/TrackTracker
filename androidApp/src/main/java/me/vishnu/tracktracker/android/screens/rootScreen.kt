package me.vishnu.tracktracker.android.screens

import NavigationEvents
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun RootScreen(dispatch: (NavigationEvents) -> Unit) {
  Column() {
    Button(onClick = { dispatch(NavigationEvents.NavigateTo(ScreenTarget.TRACKS)) }) {
      Text("Tracks")
    }

    Button(onClick = { dispatch(NavigationEvents.NavigateTo(ScreenTarget.CARS)) }) {
      Text("Cars")
    }
  }
}