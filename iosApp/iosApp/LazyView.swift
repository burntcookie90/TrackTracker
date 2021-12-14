import SwiftUI

struct LazyView<Content: View>: View {
    let builder: () -> Content
    
    init(_ builder: @escaping () -> Content) {
        self.builder = builder
    }
    
    var body: Content {
        return builder()
    }
}
