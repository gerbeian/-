import Foundation
import SwiftData
import SwiftUI

@Observable
class HomeViewModel {
    var trackItems: [TrackItem] = []
    var todayCheckIns: [CheckIn] = []
    var dailyQuote: Quote?
    var totalCheckInDays: Int = 0
    var consecutiveDays: Int = 0
    var zhengQiScore: Int = 0
    var todayCompletionRate: Double = 0
    var isLoading = false

    private let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "yyyy-MM-dd"
        return f
    }()

    func loadData(context: ModelContext) {
        isLoading = true
        defer { isLoading = false }

        let today = dateFormatter.string(from: Date())

        let itemDesc = FetchDescriptor<TrackItem>(sortBy: [SortDescriptor(\.sortOrder)])
        trackItems = (try? context.fetch(itemDesc)) ?? []

        let ciDesc = FetchDescriptor<CheckIn>()
        let allCheckIns = (try? context.fetch(ciDesc)) ?? []
        todayCheckIns = allCheckIns.filter { $0.date == today }

        let quoteDesc = FetchDescriptor<Quote>()
        let allQuotes = (try? context.fetch(quoteDesc)) ?? []
        dailyQuote = allQuotes.randomElement()

        let successDates = allCheckIns.filter { $0.status }.compactMap { ci -> Date? in
            return dateFormatter.date(from: ci.date)
        }
        let uniqueSuccessDates = Set(successDates.map { Calendar.current.startOfDay(for: $0) })
        totalCheckInDays = uniqueSuccessDates.count

        consecutiveDays = ZhengQiCalculator.getCurrentStreak(from: allCheckIns)

        let todayItems = trackItems.filter { $0.isActive }
        let completedToday = todayCheckIns.filter { $0.status }.count
        todayCompletionRate = todayItems.isEmpty ? 0 : Double(completedToday) / Double(todayItems.count)

        zhengQiScore = ZhengQiCalculator.calculateZhengQiScore(
            totalCheckInDays: totalCheckInDays,
            consecutiveDays: consecutiveDays,
            todayCompletionRate: todayCompletionRate
        )
    }

    func toggleCheckIn(for item: TrackItem, context: ModelContext) {
        let today = dateFormatter.string(from: Date())

        let desc = FetchDescriptor<CheckIn>(predicate: #Predicate { $0.date == today })
        let todayCheckIns = (try? context.fetch(desc)) ?? []
        let existing = todayCheckIns.filter { $0.trackItem?.id == item.id }

        if let checkIn = existing.first {
            checkIn.status.toggle()
            checkIn.createdAt = Date()
        } else {
            let newCheckIn = CheckIn(trackItem: item, date: today, status: true)
            context.insert(newCheckIn)
        }
        try? context.save()
        loadData(context: context)
    }

    func batchCheckIn(trackItemIds: [UUID], note: String, imageUri: String, context: ModelContext) {
        let today = dateFormatter.string(from: Date())

        // Load image data from file path if provided
        var imageData: Data? = nil
        if !imageUri.isEmpty {
            imageData = try? Data(contentsOf: URL(fileURLWithPath: imageUri))
        }

        for itemId in trackItemIds {
            let desc = FetchDescriptor<CheckIn>(predicate: #Predicate { $0.date == today })
            let todayCheckIns = (try? context.fetch(desc)) ?? []
            let existing = todayCheckIns.filter { $0.trackItem?.id == itemId }

            if let checkIn = existing.first {
                checkIn.status = true
                checkIn.note = note
                checkIn.imageData = imageData
                checkIn.createdAt = Date()
            } else {
                let itemDesc = FetchDescriptor<TrackItem>(
                    predicate: #Predicate { $0.id == itemId }
                )
                if let item = (try? context.fetch(itemDesc))?.first {
                    let newCheckIn = CheckIn(trackItem: item, date: today, status: true)
                    newCheckIn.note = note
                    newCheckIn.imageData = imageData
                    context.insert(newCheckIn)
                }
            }
        }
        try? context.save()
        loadData(context: context)
    }
}