package me.vishnu.tracktracker.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import me.vishnu.tracktracker.android.screens.CarScreen
import me.vishnu.tracktracker.android.ui.theme.AppTheme
import me.vishnu.tracktracker.shared.DriverFactory
import me.vishnu.tracktracker.shared.graph.InjectAppComponent
import me.vishnu.tracktracker.shared.graph.InjectCarScreenComponent
import me.vishnu.tracktracker.shared.stores.Loop
import me.vishnu.tracktracker.shared.stores.welcome.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val component = InjectAppComponent(DriverFactory(this))
    val carScreenComponent = InjectCarScreenComponent(component)
    val (loopState, dispatch) = Loop(
      store = carScreenComponent.carScreenStore,
      effectHandler = carScreenComponent.carScreenEffectHandler,
    ) { setOf(CarScreenEffects.LoadInitialData) }

    setContent {
      AppTheme {
        CarScreen(
          state = loopState.collectAsState(initial = CarScreenModel()).value,
          dispatch = dispatch
        )
      }
    }
  }
}

