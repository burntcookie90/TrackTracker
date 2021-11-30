package me.vishnu.tracktracker.shared

import me.vishnu.tracktracker.shared.stores.welcome.CarScreenStore

fun CarScreenStore.watchState() = observeState().wrap()
fun CarScreenStore.watchSideEffect() = observeSideEffect().wrap()