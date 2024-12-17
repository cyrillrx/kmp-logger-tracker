import SwiftUI
import KMPLogger
import KMPTracker

struct ContentView: View {
    @State private var showContent = false
    var body: some View {
        VStack {
            Button("Click me!") {
                withAnimation {
                    showContent = !showContent
                }

                KMPTracker.shared.track(event: TrackEvent(name: "button_clicked"))
            }.onAppear {
                KMPLogger.shared.info(tag:"Demo", message:"ContentView appeared", throwable: nil)
                
                let event = TrackEvent(name: "ScreenView", attributes: ["screen": "ContentView"])
                KMPTracker.shared.track(event: event)
            }

            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 200))
                        .foregroundColor(.accentColor)
                    Text("SwiftUI: Hello")
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
