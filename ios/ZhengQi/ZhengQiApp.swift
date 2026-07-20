import SwiftUI
import SwiftData

@main
struct ZhengQiApp: App {
    @AppStorage("lockEnabled") var lockEnabled: Bool = false
    @State private var showLock = false
    @Environment(\.scenePhase) var scenePhase

    var sharedModelContainer: ModelContainer = {
        let schema = Schema([
            TrackItem.self,
            CheckIn.self,
            Quote.self,
            Article.self
        ])
        let modelConfiguration = ModelConfiguration(schema: schema, isStoredInMemoryOnly: false)
        do {
            let container = try ModelContainer(for: schema, configurations: [modelConfiguration])
            Task { @MainActor in
                SeedDataService.seedIfNeeded(context: container.mainContext)
            }
            return container
        } catch {
            fatalError("Could not create ModelContainer: \(error)")
        }
    }()

    var body: some Scene {
        WindowGroup {
            ZStack {
                if lockEnabled && showLock && KeychainManager.shared.read(key: "zhengqi_passcode") != nil {
                    LockView {
                        withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                            showLock = false
                        }
                    }
                    .transition(.opacity)
                } else {
                    ContentView()
                }
            }
            .animation(.spring(response: 0.5, dampingFraction: 0.7), value: showLock)
            .onChange(of: scenePhase) { _, newPhase in
                if newPhase == .background {
                    if lockEnabled && KeychainManager.shared.read(key: "zhengqi_passcode") != nil {
                        showLock = true
                    }
                }
            }
        }
        .modelContainer(sharedModelContainer)
    }
}