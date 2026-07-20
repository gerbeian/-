import Foundation
import SwiftData
import SwiftUI

@Observable
class CalendarViewModel {
    var currentMonth: Date = Date()
    var selectedDate: Date?
    var allCheckIns: [CheckIn] = []
    var trackItems: [TrackItem] = []

    private let dateFormatter: DateFormatter = {
        let f = DateFormatter()
        f.dateFormat = "yyyy-MM-dd"
        return f
    }()

    var monthTitle: String {
        let f = DateFormatter()
        f.dateFormat = "yyyy年M月"
        return f.string(from: currentMonth)
    }

    func loadData(context: ModelContext) {
        let ciDesc = FetchDescriptor<CheckIn>()
        allCheckIns = (try? context.fetch(ciDesc)) ?? []

        let itemDesc = FetchDescriptor<TrackItem>(sortBy: [SortDescriptor(\.sortOrder)])
        trackItems = (try? context.fetch(itemDesc)) ?? []
    }

    func daysInMonth() -> [Date] {
        let calendar = Calendar.current
        guard let range = calendar.range(of: .day, in: .month, for: currentMonth),
              let firstDay = calendar.date(from: calendar.dateComponents([.year, .month], from: currentMonth)) else {
            return []
        }
        let weekday = calendar.component(.weekday, from: firstDay) - 1
        let leading = Array(repeating: Date.distantPast, count: weekday)
        let days = (1...range.count).compactMap { day -> Date? in
            calendar.date(byAdding: .day, value: day - 1, to: firstDay)
        }
        return leading + days
    }

    func dayStatus(for date: Date) -> DayStatus {
        let dateStr = dateFormatter.string(from: date)
        let activeItems = trackItems.filter { $0.isActive }
        let dayCheckIns = allCheckIns.filter { $0.date == dateStr }
        if dayCheckIns.isEmpty { return .none }
        let completed = dayCheckIns.filter { $0.status }.count
        if completed == 0 { return .none }
        if completed >= activeItems.count { return .full }
        return .partial
    }

    func checkInsForDate(_ date: Date) -> [CheckIn] {
        let dateStr = dateFormatter.string(from: date)
        return allCheckIns.filter { $0.date == dateStr }
    }

    func goToPreviousMonth() {
        let calendar = Calendar.current
        if let prev = calendar.date(byAdding: .month, value: -1, to: currentMonth) {
            currentMonth = prev
        }
    }

    func goToNextMonth() {
        let calendar = Calendar.current
        if let next = calendar.date(byAdding: .month, value: 1, to: currentMonth) {
            currentMonth = next
        }
    }
}

enum DayStatus {
    case none, partial, full
}