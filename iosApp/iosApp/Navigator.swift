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
    eventSources: [Flow](),
    logTag: nil
  )
  
  return Navigator(component: component).environmentObject(obsStore)
}

struct Navigator: ConnectedView {
  typealias S = NavigationStore
  typealias M = NavigationModel
  typealias E = NavigationEvents
  typealias F = NavigationEffects
  
  let component: AppComponent
  
  struct Props {
    let navStack: [ScreenTarget]
    let navigationDispatch: (ScreenTarget) -> Void
  }
  
  
  func map(state: NavigationModel, dispatch: @escaping DispatchFunction<NavigationEvents>) -> Props {
    return Props(
      navStack: state.navStack,
      navigationDispatch: { target in dispatch(NavigationEvents.NavigateTo(target: target)) }
    )
  }
  
  
  func body(props: Props) -> some View {
    NavigationView{
        switch props.navStack.last {
        case ScreenTarget.root:
          LazyView { rootScreen(navigationDispatch: props.navigationDispatch) }
        case ScreenTarget.cars:
          LazyView { carScreen(component: component) }
        case ScreenTarget.tracks:
          LazyView { rootScreen(navigationDispatch: props.navigationDispatch) }
        case .none, .some(_):
          Text("Error")
        }
    }
  }
  
}
