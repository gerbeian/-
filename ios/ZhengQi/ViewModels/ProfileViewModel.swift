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
    var exportResult: String?

    private let defaults = UserDefaults.standard

    var storedNickname: String {
        get { defaults.string(forKey: "userNickname") ?? "正气少年" }
        set { defaults.set(newValue, forKey: "userNickname") }
    }
    var lockEnabled: Bool {
        get { defaults.bool(forKey: "lockEnabled") }
        set { defaults.set(newValue, forKey: "lockEnabled") }
    }
    var avatarColorHex: String {
        get { defaults.string(forKey: "avatarColorHex") ?? "0066cc" }
        set { defaults.set(newValue, forKey: "avatarColorHex") }
    }

    var level: (name: String, icon: String) {
        ZhengQiCalculator.getLevel(score: zhengQiScore)
    }

    private let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "yyyy-MM-dd"
        return f
    }()

    func loadData(context: ModelContext) {
        nickname = storedNickname.isEmpty ? "正气少年" : storedNickname

        let ciDesc = FetchDescriptor<CheckIn>()
        allCheckIns = (try? context.fetch(ciDesc)) ?? []

        let itemDesc = FetchDescriptor<TrackItem>(sortBy: [SortDescriptor(\.sortOrder)])
        trackItems = (try? context.fetch(itemDesc)) ?? []

        let successDates = allCheckIns.filter { $0.status }.compactMap { ci -> Date? in
            return dateFormatter.date(from: ci.date)
        }
        let uniqueSuccessDates = Set(successDates.map { Calendar.current.startOfDay(for: $0) })
        totalCheckInDays = uniqueSuccessDates.count

        consecutiveDays = ZhengQiCalculator.getCurrentStreak(from: allCheckIns)

        let todayStr = dateFormatter.string(from: Date())
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

    @MainActor
    func deleteAllData(context: ModelContext) {
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

    func exportData(context: ModelContext) {
        do {
            let ciDesc = FetchDescriptor<CheckIn>()
            let allCheckIns = (try? context.fetch(ciDesc)) ?? []

            let itemDesc = FetchDescriptor<TrackItem>()
            let allTrackItems = (try? context.fetch(itemDesc)) ?? []
            let trackItemMap = Dictionary(uniqueKeysWithValues: allTrackItems.map { ($0.id, $0.name) })

            var jsonArray: [[String: Any]] = []
            for checkIn in allCheckIns {
                var obj: [String: Any] = [
                    "date": checkIn.date,
                    "trackItem": trackItemMap[checkIn.trackItem?.id ?? UUID()] ?? "未知",
                    "status": checkIn.status,
                    "note": checkIn.note
                ]
                if let imageData = checkIn.imageData {
                    obj["imageDataSize"] = imageData.count
                }
                jsonArray.append(obj)
            }

            let jsonData = try JSONSerialization.data(withJSONObject: jsonArray, options: .prettyPrinted)
            let todayStr = dateFormatter.string(from: Date())

            let downloadsDir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
            let fileURL = downloadsDir.appendingPathComponent("正气_打卡数据_\(todayStr).json")
            try jsonData.write(to: fileURL)

            exportResult = "导出成功：\(fileURL.path)"
        } catch {
            exportResult = "导出失败：\(error.localizedDescription)"
        }
    }

    func clearExportResult() {
        exportResult = nil
    }
}