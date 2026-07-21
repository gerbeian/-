import SwiftUI

struct ContentView: View {
    @State private var selectedTab = 0
    @State private var showEmergency = false

    var body: some View {
        TabView(selection: $selectedTab) {
            NavigationStack {
                HomeView(showEmergency: $showEmergency)
            }
            .tabItem {
                Image(systemName: selectedTab == 0 ? "house.fill" : "house")
                Text("首页")
            }
            .tag(0)

            NavigationStack {
                CalendarView()
            }
            .tabItem {
                Image(systemName: selectedTab == 1 ? "calendar.circle.fill" : "calendar")
                Text("日历")
            }
            .tag(1)

            NavigationStack {
                LearnView()
            }
            .tabItem {
                Image(systemName: selectedTab == 2 ? "book.fill" : "book")
                Text("学习")
            }
            .tag(2)

            NavigationStack {
                ProfileView()
            }
            .tabItem {
                Image(systemName: selectedTab == 3 ? "person.fill" : "person")
                Text("我的")
            }
            .tag(3)
        }
        .tint(.primaryBlue)
        .animation(.spring(response: 0.5, dampingFraction: 0.7), value: selectedTab)
        .fullScreenCover(isPresented: $showEmergency) {
            NavigationStack {
                EmergencyView()
            }
        }
    }
}