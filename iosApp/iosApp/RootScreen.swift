//
//  RootScreen.swift
//  iosApp
//
//  Created by Vishnu Rajeevan on 12/14/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

func rootScreen(navigationDispatch: @escaping (ScreenTarget) -> Void) -> some View {
  return RootScreen(navigationDispatch: navigationDispatch)
}

struct RootScreen: View {
  let navigationDispatch: (ScreenTarget) -> Void
  
  var body : some View {
    VStack {
      Button(action: {navigationDispatch(ScreenTarget.cars)} ) {
        Text("Cars")
      }
      Button(action: {navigationDispatch(ScreenTarget.tracks)} ) {
        Text("Track")
      }
    }
  }
}
