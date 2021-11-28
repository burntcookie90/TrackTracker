package me.vishnu.tracktracker.shared

import me.vishnu.tracktracker.shared.stores.Effect
import me.vishnu.tracktracker.shared.stores.Event
import me.vishnu.tracktracker.shared.stores.Model
import me.vishnu.tracktracker.shared.stores.Store

fun <M: Model, E: Event, F: Effect> Store<M, E, F>.watchState() = observeState().wrap()
fun <M: Model, E: Event, F: Effect> Store<M, E, F>.watchSideEffect() = observeSideEffect().wrap()