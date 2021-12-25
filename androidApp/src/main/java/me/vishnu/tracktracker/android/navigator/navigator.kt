package me.vishnu.tracktracker.android.navigator

import NavigationEvents
import NavigationModel
import ScreenTarget
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import me.vishnu.tracktracker.android.screens.CarScreen
import me.vishnu.tracktracker.android.screens.RootScreen
import me.vishnu.tracktracker.android.screens.TrackScreen
import me.vishnu.tracktracker.shared.graph.AppComponent
import me.vishnu.tracktracker.shared.graph.CarScreenComponent
import me.vishnu.tracktracker.shared.graph.TrackScreenComponent
import me.vishnu.tracktracker.shared.graph.create
import me.vishnu.tracktracker.shared.stores.Loop


@Composable
fun Navigator(component: AppComponent, backButton: Flow<NavigationEvents.GoBack>) {
  val store = component.navigationStore
  val effectHandler = component.navigationEffectHandler

  val (loopState, dispatch) = remember {
    Loop(
      store = store,
      effectHandler = effectHandler,
      eventSources = listOf(backButton),
      logTag = "Navigator"
    )
  }

  Navigator(
    component,
    loopState.collectAsState().value,
    dispatch
  )
}

@Composable
fun Navigator(
  appComponent: AppComponent,
  model: NavigationModel,
  dispatch: (NavigationEvents) -> Unit
) {
  val component = remember { appComponent }
  when (model.navStack.last()) {
    ScreenTarget.ROOT -> RootScreen(dispatch = dispatch)
    ScreenTarget.TRACKS -> {
      val trackScreenComponent = remember { TrackScreenComponent::class.create(component) }
      TrackScreen(
        store = trackScreenComponent.trackScreenStore,
        effectHandler = trackScreenComponent.trackScreenEffectHandler
      )
    }
    ScreenTarget.CARS -> {
      val carScreenComponent = remember { CarScreenComponent::class.create(component) }
      CarScreen(
        carScreenStore = carScreenComponent.carScreenStore,
        carScreenEffectHandler = carScreenComponent.carScreenEffectHandler
      )
    }
  }
}