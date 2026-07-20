import Foundation
import SwiftData
import SwiftUI

@Observable
class ProfileViewModel {
    var nickname: String = ""
    var avatarColor: Color = Color.primaryBlue
    var totalCheckInDays: Int = 0
    var consecutiveDays: Int = 0
    var zhengQiScore: Int = 0
    var allCheckIns: [CheckIn] = []
    var trackItems: [TrackItem] = []

    @AppStorage("userNickname") var storedNickname: String = "正气少年"
    @AppStorage("lockEnabled") var lockEnabled: Bool = false
    @AppStorage("avatarColorHex") var avatarColorHex: String = "0066cc"

    var level: (name: String, icon: String) {
        ZhengQiCalculator.getLevel(score: zhengQiScore)
    }

    func loadData(context: ModelContext) {
        nickname = storedNickname.isEmpty ? "正气少年" : storedNickname

        let ciDesc = FetchDescriptor<CheckIn>()
        allCheckIns = (try? context.fetch(ciDesc)) ?? []

        let itemDesc = FetchDescriptor<TrackItem>(sortBy: [SortDescriptor(\.sortOrder)])
        trackItems = (try? context.fetch(itemDesc)) ?? []

        let successDates = allCheckIns.filter { $0.status }.compactMap { ci -> Date? in
            let f = DateFormatter()
            f.dateFormat = "yyyy-MM-dd"
            return f.date(from: ci.date)
        }
        let uniqueSuccessDates = Set(successDates.map { Calendar.current.startOfDay(for: $0) })
        totalCheckInDays = uniqueSuccessDates.count

        consecutiveDays = ZhengQiCalculator.getCurrentStreak(from: allCheckIns)

        let today = DateFormatter()
        today.dateFormat = "yyyy-MM-dd"
        let todayStr = today.string(from: Date())
        let todayItems = trackItems.filter { $0.isActive }
        let todayCheckIns = allCheckIns.filter { $0.date == todayStr }
        let completedToday = todayCheckIns.filter { $0.status }.count
        let rate = todayItems.isEmpty ? 0 : Double(completedToday) / Double(todayItems.count)

        zhengQiScore = ZhengQiCalculator.calculateZhengQiScore(
            totalCheckInDays: totalCheckInDays,
            consecutiveDays: consecutiveDays,
            todayCompletionRate: rate
        )
    }

    func saveNickname() {
        storedNickname = nickname
    }

    func clearAllData(context: ModelContext) {
        do {
            try context.delete(model: CheckIn.self)
            try context.delete(model: TrackItem.self)
            try context.delete(model: Quote.self)
            try context.delete(model: Article.self)
            try context.save()
            SeedDataService.seedIfNeeded(context: context)
        } catch {
            print("Failed to clear data: \(error)")
        }
    }
}