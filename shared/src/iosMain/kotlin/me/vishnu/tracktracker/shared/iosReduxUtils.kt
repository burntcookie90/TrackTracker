package me.vishnu.tracktracker.shared

import me.vishnu.tracktracker.shared.stores.welcome.WelcomeStore

fun WelcomeStore.watchState() = observeState().wrap()
fun WelcomeStore.watchSideEffect() = observeSideEffect().wrap()