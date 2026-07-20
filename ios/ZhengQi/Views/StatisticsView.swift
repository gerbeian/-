import SwiftUI
import SwiftData

struct StatisticsView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var selectedPeriod: StatsPeriod = .week
    @State private var allCheckIns: [CheckIn] = []
    @State private var trackItems: [TrackItem] = []

    enum StatsPeriod: String, CaseIterable {
        case week = "周"
        case month = "月"
        case year = "年"
    }

    var body: some View {
        ScrollView {
            VStack(spacing: ZQSpacing.lg) {
                Picker("周期", selection: $selectedPeriod) {
                    ForEach(StatsPeriod.allCases, id: \.self) { period in
                        Text(period.rawValue).tag(period)
                    }
                }
                .pickerStyle(.segmented)
                .padding(.horizontal, ZQSpacing.lg)

                switch selectedPeriod {
                case .week:
                    weeklyChart
                case .month:
                    monthlyChart
                case .year:
                    yearlyHeatmap
                }
            }
            .padding(.vertical, ZQSpacing.md)
        }
        .background(Color.canvasParchment)
        .navigationTitle("统计图表")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear { loadData() }
    }

    private var weeklyChart: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            Text("本周每日打卡数")
                .font(.zqTitle2)
                .padding(.horizontal, ZQSpacing.lg)

            WeeklyBarChart(data: weeklyData())
                .padding(.horizontal, ZQSpacing.lg)
        }
        .padding(.vertical, ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(Color.canvas)
        )
        .padding(.horizontal, ZQSpacing.lg)
    }

    private var monthlyChart: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            Text("本月完成率趋势")
                .font(.zqTitle2)
                .padding(.horizontal, ZQSpacing.lg)

            MonthlyLineChart(data: monthlyData())
                .padding(.horizontal, ZQSpacing.lg)
        }
        .padding(.vertical, ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(Color.canvas)
        )
        .padding(.horizontal, ZQSpacing.lg)
    }

    private var yearlyHeatmap: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            Text("年度打卡热力图")
                .font(.zqTitle2)
                .padding(.horizontal, ZQSpacing.lg)

            YearHeatmapView(data: yearlyData())
                .padding(.horizontal, ZQSpacing.lg)
        }
        .padding(.vertical, ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(Color.canvas)
        )
        .padding(.horizontal, ZQSpacing.lg)
    }

    private func loadData() {
        let ciDesc = FetchDescriptor<CheckIn>()
        allCheckIns = (try? modelContext.fetch(ciDesc)) ?? []

        let itemDesc = FetchDescriptor<TrackItem>(sortBy: [SortDescriptor(\.sortOrder)])
        trackItems = (try? modelContext.fetch(itemDesc)) ?? []
    }

    private func weeklyData() -> [(day: String, count: Int)] {
        let calendar = Calendar.current
        let weekDays = ["日", "一", "二", "三", "四", "五", "六"]
        let today = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"

        var result: [(day: String, count: Int)] = []
        for i in (0..<7).reversed() {
            guard let date = calendar.date(byAdding: .day, value: -i, to: today) else { continue }
            let dayStr = weekDays[calendar.component(.weekday, from: date) - 1]
            let dateStr = dateFormatter.string(from: date)
            let count = allCheckIns.filter { $0.date == dateStr && $0.status }.count
            result.append((day: dayStr, count: count))
        }
        return result
    }

    private func monthlyData() -> [(day: String, rate: Double)] {
        let calendar = Calendar.current
        let today = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let activeCount = trackItems.filter { $0.isActive }.count

        guard let range = calendar.range(of: .day, in: .month, for: today) else { return [] }
        let components = calendar.dateComponents([.year, .month], from: today)
        guard let firstDay = calendar.date(from: components) else { return [] }

        var result: [(day: String, rate: Double)] = []
        for day in 1...range.count {
            guard let date = calendar.date(byAdding: .day, value: day - 1, to: firstDay) else { continue }
            let dateStr = dateFormatter.string(from: date)
            let completed = allCheckIns.filter { $0.date == dateStr && $0.status }.count
            let rate = activeCount > 0 ? Double(completed) / Double(activeCount) : 0
            result.append((day: "\(day)", rate: rate))
        }
        return result
    }

    private func yearlyData() -> [(date: Date, count: Int)] {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"

        let grouped = Dictionary(grouping: allCheckIns.filter { $0.status }) { $0.date }
        return grouped.compactMap { dateStr, checkIns -> (date: Date, count: Int)? in
            guard let date = dateFormatter.date(from: dateStr) else { return nil }
            return (date: date, count: checkIns.count)
        }
    }
}