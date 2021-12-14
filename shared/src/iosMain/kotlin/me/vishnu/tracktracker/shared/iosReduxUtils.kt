package me.vishnu.tracktracker.shared

import NavigationStore
import me.vishnu.tracktracker.shared.stores.welcome.CarScreenStore

fun CarScreenStore.watchState() = observeState().wrap()
fun CarScreenStore.watchSideEffect() = observeSideEffect().wrap()

fun NavigationStore.watchState() = observeState().wrap()
fun NavigationStore.watchSideEffect() = observeSideEffect().wrap()
