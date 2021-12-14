//
//  Navigator.swift
//  iosApp
//
//  Created by Vishnu Rajeevan on 12/13/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

func navigator(component: AppComponent) -> some View{
  typealias ObservableNavigator = ObservableStore<NavigationStore, NavigationModel, NavigationEvents, NavigationEffects>
  let obsStore: ObservableNavigator
  
  let store = component.navigationStore
  let effectHandler = component.navigationEffectHandler
  obsStore = ObservableNavigator(
    store: store,
    state: NavigationModel.Companion.shared.defaultModel(),
    stateWatcher: store.watchState(),
    sideEffectWatcher: store.watchSideEffect()
  )
  
  _ = Loop<NavigationModel, NavigationEvents, NavigationEffects, NavigationEffectHandler>(
    store: store,
    effectHandler: effectHandler,
    startEffects: { Set() },
    eventSources: [Flow]())
  
  return Navigator().environmentObject(obsStore)
}

struct Navigator: ConnectedView {
  typealias S = NavigationStore
  typealias M = NavigationModel
  typealias E = NavigationEvents
  typealias F = NavigationEffects
  
  struct Props {
    let navStack: Set<ScreenTarget>
  }
  
  
  func map(state: NavigationModel, dispatch: @escaping DispatchFunction<NavigationEvents>) -> Props {
    return Props(
      navStack: state.navStack
    )
  }
  
  
  func body(props: Props) -> some View {
    NavigationView{}
  }
  
}
