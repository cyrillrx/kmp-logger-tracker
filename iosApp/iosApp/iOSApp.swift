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

        KMPLogger.shared.info(tag: "iOSApp", message: "Logger initialized", throwable: nil)
    }

    private func initTracker() {
        let appName: String = Bundle.main.bundleIdentifier ?? ""
        let appVersion: String = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? ""
        let app = TrackingApp(name: appName, version: appVersion)

        KMPTracker.shared.setupApp(app: app)
        KMPTracker.shared.setupExceptionCatcher(
            catchException: { e in
                KMPLogger.shared.info(tag: "iOSApp", message: "Tracker failed", throwable: nil)
            }
        )
        
        KMPLogger.shared.info(tag: "iOSApp", message: "Tracker initialized", throwable: nil)
    }
}
