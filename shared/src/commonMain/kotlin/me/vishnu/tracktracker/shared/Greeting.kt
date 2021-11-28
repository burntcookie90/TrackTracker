package me.vishnu.tracktracker.shared

class Greeting {
  fun greeting(): String {
    return "Hello, ${Platform().platform}!"
  }
}