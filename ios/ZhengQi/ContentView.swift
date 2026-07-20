import SwiftUI

struct ContentView: View {
    @State private var selectedTab = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            HomeView()
                .tabItem {
                    Image(systemName: selectedTab == 0 ? "house.fill" : "house")
                    Text("首页")
                }
                .tag(0)

            CalendarView()
                .tabItem {
                    Image(systemName: selectedTab == 1 ? "calendar.circle.fill" : "calendar")
                    Text("日历")
                }
                .tag(1)

            CommunityView()
                .tabItem {
                    Image(systemName: selectedTab == 2 ? "person.3.fill" : "person.3")
                    Text("社区")
                }
                .tag(2)

            LearnView()
                .tabItem {
                    Image(systemName: selectedTab == 3 ? "book.fill" : "book")
                    Text("学习")
                }
                .tag(3)

            ProfileView()
                .tabItem {
                    Image(systemName: selectedTab == 4 ? "person.fill" : "person")
                    Text("我的")
                }
                .tag(4)
        }
        .tint(.primaryBlue)
        .animation(.spring(response: 0.5, dampingFraction: 0.7), value: selectedTab)
    }
}