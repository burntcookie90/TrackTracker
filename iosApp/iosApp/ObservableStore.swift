//
//  ObservableStore.swift
//  iosApp
//
//  Created by Vishnu Rajeevan on 11/28/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class ObservableStore<S: Store, M: Model, E: Event, F: Effect>: ObservableObject {
  @Published public var state: M
  @Published public var sideEffect: F?
  
  let store: S
  
  var stateWatcher: Closeable?
  var sideEffectWatcher: Closeable?
  
  init(store: S, state: M, stateWatcher: CFlow<M>, sideEffectWatcher: CFlow<F>) {
    self.store = store
    self.state = state
    
    self.stateWatcher = stateWatcher.watch { [weak self] state in
      print("new state \(state)")
      self?.state = state
    }
    
    self.sideEffectWatcher = sideEffectWatcher.watch { [weak self] effect in
      print("new effects \(effect)")
      self?.sideEffect = effect
    }
  }
  
  public func dispatch(_ event: E) {
    store.dispatch(event: event)
  }
  
  deinit {
    stateWatcher?.close()
    sideEffectWatcher?.close()
  }
}

public typealias DispatchFunction<E: Event> = (E) -> ()

public protocol ConnectedView : View {
  associatedtype S : Store
  associatedtype M : Model
  associatedtype E : Event
  associatedtype F: Effect
  associatedtype Props
  associatedtype V: View
  
  func map(state: M, dispatch: @escaping DispatchFunction<E>) -> Props
  func body(props: Props) -> V
}

public extension ConnectedView {
  func render(state: M, dispatch: @escaping DispatchFunction<E>) -> V {
    let props = map(state: state, dispatch: dispatch)
    return body(props: props)
  }
  
  var body: StoreConnector<S, M, E, F, V> {
    return StoreConnector(content: render)
  }
}

public struct StoreConnector<S: Store, M: Model, E: Event, F: Effect, V: View>: View {
  @EnvironmentObject var store: ObservableStore<S, M, E, F>
  let content: (M, @escaping DispatchFunction<E>) -> V
  
  public var body: V {
    return content(store.state, store.dispatch)
  }
}

