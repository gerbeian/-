import Foundation
import SwiftData

struct ZhengQiCalculator {
    static func calculateZhengQiScore(totalCheckInDays: Int, consecutiveDays: Int, todayCompletionRate: Double) -> Int {
        let baseScore = totalCheckInDays * 10
        let consecutiveBonus = consecutiveDays * consecutiveDays * 2
        let completionBonus = Int(todayCompletionRate * 50)
        return baseScore + consecutiveBonus + completionBonus
    }

    static func getLevel(score: Int) -> (name: String, icon: String) {
        switch score {
        case 0..<100: return ("初识正气", "leaf.fill")
        case 100..<300: return ("修身养性", "sparkles")
        case 300..<600: return ("正气渐长", "flame.fill")
        case 600..<1000: return ("正气浩然", "star.fill")
        case 1000..<2000: return ("正气磅礴", "bolt.fill")
        case 2000..<5000: return ("天人合一", "crown.fill")
        default: return ("正气无极", "infinity")
        }
    }

    static func getMaxConsecutiveDays(from checkIns: [CheckIn], trackItems: [TrackItem]) -> Int {
        let allDates = Set(checkIns.filter { $0.status }.map { $0.date })
        let sorted = allDates.sorted()
        guard !sorted.isEmpty else { return 0 }

        var maxStreak = 0
        var currentStreak = 1
        let calendar = Calendar.current
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"

        for i in 1..<sorted.count {
            guard let prev = dateFormatter.date(from: sorted[i-1]),
                  let curr = dateFormatter.date(from: sorted[i]) else { continue }
            if calendar.date(byAdding: .day, value: 1, to: prev) == curr {
                currentStreak += 1
            } else {
                maxStreak = max(maxStreak, currentStreak)
                currentStreak = 1
            }
        }
        maxStreak = max(maxStreak, currentStreak)
        return maxStreak
    }

    static func getCurrentStreak(from checkIns: [CheckIn]) -> Int {
        let dates = checkIns.filter { $0.status }.compactMap { ci -> Date? in
            let f = DateFormatter()
            f.dateFormat = "yyyy-MM-dd"
            return f.date(from: ci.date)
        }
        let uniqueDates = Set(dates.map { Calendar.current.startOfDay(for: $0) }).sorted(by: >)
        guard !uniqueDates.isEmpty else { return 0 }

        let today = Calendar.current.startOfDay(for: Date())
        guard let mostRecent = uniqueDates.first else { return 0 }
        let diff = Calendar.current.dateComponents([.day], from: mostRecent, to: today).day ?? 0
        if diff > 1 { return 0 }

        var streak = 1
        for i in 1..<uniqueDates.count {
            if Calendar.current.dateComponents([.day], from: uniqueDates[i], to: uniqueDates[i-1]).day == 1 {
                streak += 1
            } else {
                break
            }
        }
        return streak
    }
}