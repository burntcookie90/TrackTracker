package me.vishnu.tracktracker.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import me.vishnu.tracktracker.android.ui.theme.AppTheme
import me.vishnu.tracktracker.shared.DriverFactory
import me.vishnu.tracktracker.shared.createDatabase
import me.vishnu.tracktracker.shared.initLogger
import me.vishnu.tracktracker.shared.models.UiCar
import me.vishnu.tracktracker.shared.models.titleDisplay
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.RealDataModifier
import me.vishnu.tracktracker.shared.repos.CarRepo
import me.vishnu.tracktracker.shared.stores.Loop
import me.vishnu.tracktracker.shared.stores.welcome.*

class MainActivity : ComponentActivity() {
  private lateinit var dataModifier: DataModifier

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initLogger()

    val db = createDatabase(DriverFactory(this))
    dataModifier = RealDataModifier(db)
    val carRepo = CarRepo(db.carQueries)
    val (loop, eventCallback) =
      Loop(
        WelcomeStore,
        WelcomeEffectHandler(carRepo = carRepo, modifier = dataModifier)
      ) {
        setOf(WelcomeEffects.LoadInitialData)
      }

    setContent {
      AppTheme {
        val state = loop.collectAsState(initial = WelcomeModel())
        Log.d("Meow", "${state.value}")
        CarScreen(state.value, eventCallback)
      }
    }
  }
}

@Composable
fun CarScreen(state: WelcomeModel, eventCallback: (WelcomeEvents) -> Unit) {
  Scaffold(
    topBar = { TopAppBar(title = { Text("Track Tracker") }) },
    floatingActionButton = {
      if (!state.addCarMode) {
        FloatingActionButton(onClick = {
          eventCallback(WelcomeEvents.AddCar)
        }) { Icon(Icons.Default.Add, contentDescription = "Add Car Button") }
      }
    }
  ) {
    if (state.addCarMode) {
      CarScreenAddCar(eventCallback)
    } else {
      CarScreenDisplay(state)
    }
  }
}

@Composable
private fun CarScreenAddCar(eventCallback: (WelcomeEvents) -> Unit) {
  val year = remember { mutableStateOf(2021) }
  val make = remember { mutableStateOf("") }
  val model = remember { mutableStateOf("") }
  val trim = remember { mutableStateOf<String?>("") }
  val nickname = remember { mutableStateOf<String?>("") }
  Column {
    TextField(
      value = year.value.toString(),
      onValueChange = { year.value = it.toInt() },
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
      label = {
        Text("Year")
      }
    )

    TextField(value = make.value, onValueChange = { make.value = it }, label = {
      Text("Make")
    })
    TextField(value = model.value, onValueChange = { model.value = it }, label = {
      Text("Model")
    })
    TextField(value = trim.value.orEmpty(), onValueChange = { trim.value = it }, label = {
      Text("Trim")
    })
    TextField(value = nickname.value.orEmpty(), onValueChange = { nickname.value = it }, label = {
      Text("Nickname")
    })

    Row {
      Button(onClick = { eventCallback(WelcomeEvents.DismissAddCarDialog) }) {
        Text("Cancel")
      }

      Button(onClick = {
        eventCallback(
          WelcomeEvents.CreateCar(
            UiCar(
              year = year.value,
              make = make.value,
              model = model.value,
              trim = trim.value,
              nickname = nickname.value
            )
          )
        )
      }) {
        Text("Submit")
      }
    }
  }
}

@Composable
private fun CarScreenDisplay(state: WelcomeModel) {
  if (state.cars.isEmpty()) {
    Text("add some cars!")
  } else {
    LazyColumn {
      items(state.cars) { car ->
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
          Text(car.titleDisplay(), style = MaterialTheme.typography.h6)
          if (car.nickname != null) {
            Text(car.nickname!!, style = MaterialTheme.typography.subtitle1)
          }
        }
      }
    }
  }
}

