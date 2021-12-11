package me.vishnu.tracktracker.android

import NavigationEvents
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import kotlinx.coroutines.flow.MutableSharedFlow
import me.vishnu.tracktracker.android.navigator.Navigator
import me.vishnu.tracktracker.android.ui.theme.AppTheme
import me.vishnu.tracktracker.shared.DriverFactory
import me.vishnu.tracktracker.shared.graph.AppComponent
import me.vishnu.tracktracker.shared.graph.create

class MainActivity : ComponentActivity() {
  private val backButtonFlow = MutableSharedFlow<NavigationEvents.GoBack>(extraBufferCapacity = 1)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    onBackPressedDispatcher.addCallback {
      backButtonFlow.tryEmit(NavigationEvents.GoBack)
    }

    setContent {
      AppTheme {
        Navigator(AppComponent::class.create(
          driverFactory = DriverFactory(this)
        ),
          backButton = backButtonFlow
        )
      }
    }
  }

}

