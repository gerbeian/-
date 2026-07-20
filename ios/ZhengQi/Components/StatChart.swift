import SwiftUI
import Charts

struct WeeklyBarChart: View {
    let data: [(day: String, count: Int)]

    var body: some View {
        Chart {
            ForEach(data, id: \.day) { item in
                BarMark(
                    x: .value("日期", item.day),
                    y: .value("次数", item.count)
                )
                .foregroundStyle(Color.primaryBlue.gradient)
                .cornerRadius(ZQRounded.xs)
            }
        }
        .chartYAxis {
            AxisMarks(position: .leading) { _ in
                AxisGridLine(stroke: StrokeStyle(dash: [4, 4]))
                    .foregroundStyle(Color.hairline)
                AxisValueLabel()
                    .foregroundStyle(Color.inkMuted48)
            }
        }
        .chartXAxis {
            AxisMarks { _ in
                AxisValueLabel()
                    .foregroundStyle(Color.inkMuted48)
            }
        }
        .frame(height: 200)
    }
}

struct MonthlyLineChart: View {
    let data: [(day: String, rate: Double)]

    var body: some View {
        Chart {
            ForEach(data, id: \.day) { item in
                LineMark(
                    x: .value("日期", item.day),
                    y: .value("完成率", item.rate)
                )
                .foregroundStyle(Color.primaryBlue)
                .interpolationMethod(.catmullRom)

                AreaMark(
                    x: .value("日期", item.day),
                    y: .value("完成率", item.rate)
                )
                .foregroundStyle(
                    LinearGradient(
                        colors: [Color.primaryBlue.opacity(0.3), Color.primaryBlue.opacity(0.05)],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
                .interpolationMethod(.catmullRom)
            }
        }
        .chartYScale(domain: 0...1.0)
        .chartYAxis {
            AxisMarks(position: .leading) { _ in
                AxisGridLine(stroke: StrokeStyle(dash: [4, 4]))
                    .foregroundStyle(Color.hairline)
                AxisValueLabel()
                    .foregroundStyle(Color.inkMuted48)
            }
        }
        .chartXAxis {
            AxisMarks { _ in
                AxisValueLabel()
                    .foregroundStyle(Color.inkMuted48)
            }
        }
        .frame(height: 200)
    }
}

struct YearHeatmapView: View {
    let data: [(date: Date, count: Int)]

    private let columns = Array(repeating: GridItem(.flexible(), spacing: 3), count: 7)
    private let maxCount: Int

    init(data: [(date: Date, count: Int)]) {
        self.data = data
        self.maxCount = data.map(\.count).max() ?? 1
    }

    var body: some View {
        let weeks = groupByWeeks()

        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 3) {
                ForEach(Array(weeks.enumerated()), id: \.offset) { weekIndex, week in
                    VStack(spacing: 3) {
                        ForEach(Array(week.enumerated()), id: \.offset) { dayIndex, day in
                            RoundedRectangle(cornerRadius: ZQRounded.xs)
                                .fill(heatColor(for: day?.count ?? 0))
                                .frame(width: 14, height: 14)
                        }
                    }
                }
            }
        }
    }

    private func groupByWeeks() -> [[(date: Date, count: Int)?]] {
        let calendar = Calendar.current
        let today = Date()
        guard let yearAgo = calendar.date(byAdding: .day, value: -364, to: today) else { return [] }
        guard let startOfWeek = calendar.date(from: calendar.dateComponents([.yearForWeekOfYear, .weekOfYear], from: yearAgo)) else { return [] }

        let dataMap = Dictionary(grouping: data) { calendar.startOfDay(for: $0.date) }
            .mapValues { $0.first?.count ?? 0 }

        var weeks: [[(date: Date, count: Int)?]] = []
        var currentWeek: [(date: Date, count: Int)?] = []
        var currentDate = startOfWeek

        for _ in 0..<365 {
            let weekday = calendar.component(.weekday, from: currentDate) - 1
            if weekday == 0 && !currentWeek.isEmpty {
                weeks.append(currentWeek)
                currentWeek = []
            }
            let dayStart = calendar.startOfDay(for: currentDate)
            currentWeek.append((date: dayStart, count: dataMap[dayStart] ?? 0))
            currentDate = calendar.date(byAdding: .day, value: 1, to: currentDate)!
        }
        if !currentWeek.isEmpty { weeks.append(currentWeek) }
        return weeks
    }

    private func heatColor(for count: Int) -> Color {
        let ratio = maxCount > 0 ? Double(count) / Double(maxCount) : 0
        if ratio == 0 { return Color.hairline.opacity(0.5) }
        return Color.primaryBlue.opacity(0.2 + ratio * 0.8)
    }
}