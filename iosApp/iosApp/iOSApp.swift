import SwiftUI
import KMPLogger
import KMPTracker

@main
struct iOSApp: App {
    
    init() {
        initLogger()
        initTracker()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

    private func initLogger() {
        #if DEBUG
        let logSeverity = Severity.verbose
        #else
        let logSeverity = Severity.info
        #endif

        KMPLogger.shared.addChild(child: OsLogger(severity: logSeverity))
        KMPLogger.shared.addChild(child: NsLogger(severity: logSeverity))

        KMPLogger.shared.info(tag: "Demo", message: "Logger initialized", throwable: nil)
    }

    private func initTracker() {
        let appName: String = Bundle.main.bundleIdentifier ?? ""
        let appVersion: String = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? ""
        let app = TrackingApp(name: appName, version: appVersion)

        KMPTracker.shared.setupApp(app: app)
        KMPTracker.shared.setupExceptionCatcher(
            catchException: { e in
                KMPLogger.shared.info(tag: "Demo", message: "Tracker failed", throwable: nil)
            }
        )
        
        KMPTracker.shared.addChild(child: DummyTracker())
        
        KMPLogger.shared.info(tag: "Demo", message: "Tracker initialized", throwable: nil)
    }

    class DummyTracker: TrackerChild {
        init() {
            super.init(name: "dummy_tracker")
        }
        
        override func doTrack(event: TrackEvent) {
            KMPLogger.shared.info(tag: "Demo", message: "Tracking event: \(event.name)", throwable: nil)
        }
    }
}
