package me.vishnu.tracktracker.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import me.vishnu.tracktracker.android.ui.theme.AppTheme
import me.vishnu.tracktracker.shared.DriverFactory
import me.vishnu.tracktracker.shared.createDatabase
import me.vishnu.tracktracker.shared.graph.InjectAppComponent
import me.vishnu.tracktracker.shared.graph.InjectWelcomeComponent
import me.vishnu.tracktracker.shared.initLogger
import me.vishnu.tracktracker.shared.models.UiCar
import me.vishnu.tracktracker.shared.models.titleDisplay
import me.vishnu.tracktracker.shared.modifier.DataModifier
import me.vishnu.tracktracker.shared.modifier.RealDataModifier
import me.vishnu.tracktracker.shared.repos.CarRepo
import me.vishnu.tracktracker.shared.stores.Loop
import me.vishnu.tracktracker.shared.stores.welcome.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initLogger()

    val db = createDatabase(DriverFactory(this))
    val component = InjectAppComponent(db)
    val welcomeComponent = InjectWelcomeComponent(component)
    val (loop, eventCallback) =
      Loop(
        WelcomeStore(),
        welcomeComponent.welcomeEffectHandler
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
      CarScreenAddCar(state.selectableYears, eventCallback)
    } else {
      CarScreenDisplay(state)
    }
  }
}

@Composable
private fun CarScreenAddCar(selectableYears: List<Int>, eventCallback: (WelcomeEvents) -> Unit) {
  val year = remember { mutableStateOf(selectableYears.first()) }
  val yearDisplay = if (year.value == 0) "" else year.value.toString()
  val make = remember { mutableStateOf("") }
  val model = remember { mutableStateOf("") }
  val trim = remember { mutableStateOf("") }
  val nickname = remember { mutableStateOf("") }

  val yearIsValid = selectableYears.contains(year.value)

  val yearDropdownExpanded = remember { mutableStateOf(false) }
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {

    OutlinedTextField(
      modifier = Modifier.fillMaxWidth(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      value = yearDisplay,
      isError = !yearIsValid,
      onValueChange = { newValue: String ->
        if (newValue.isNotEmpty()) {
          year.value = newValue.toInt()
        }
        else {
          year.value = 0
        }
      },
      label = { Text("Year") },
      trailingIcon = {
        Box {
          Icon(
            modifier = Modifier.clickable { yearDropdownExpanded.value = true },
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown for year selection"
          )

          DropdownMenu(
            expanded = yearDropdownExpanded.value,
            onDismissRequest = { yearDropdownExpanded.value = false }) {
            selectableYears.forEach { yearToDisplay ->
              DropdownMenuItem(onClick = {
                year.value = yearToDisplay
                yearDropdownExpanded.value = false
              }) {
                Text("$yearToDisplay")
              }
            }
          }

        }
      }
    )
    CarInputField(label = "Make", value = make)
    CarInputField(label = "Model", value = model)
    CarInputField(label = "Trim", value = trim)
    CarInputField(label = "Nickname", value = nickname)

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
private fun CarInputField(label: String, value: MutableState<String>) {
  TextField(
    modifier = Modifier.fillMaxWidth(),
    value = value.value,
    onValueChange = { value.value = it },
    label = {
      Text(label)
    }
  )
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

