import SwiftUI
import SwiftData

struct CalendarView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var viewModel = CalendarViewModel()
    @State private var showDayDetail = false

    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                HStack {
                    Button(action: { withAnimation { viewModel.goToPreviousMonth() } }) {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundColor(.primaryBlue)
                            .frame(width: 40, height: 40)
                    }

                    Text(viewModel.monthTitle)
                        .font(.zqTitle2)
                        .frame(maxWidth: .infinity)

                    Button(action: { withAnimation { viewModel.goToNextMonth() } }) {
                        Image(systemName: "chevron.right")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundColor(.primaryBlue)
                            .frame(width: 40, height: 40)
                    }
                }
                .padding(.horizontal, ZQSpacing.lg)
                .padding(.vertical, ZQSpacing.md)

                CheckInCalendar(
                    days: viewModel.daysInMonth(),
                    dayStatus: { viewModel.dayStatus(for: $0) },
                    onDayTap: { date in
                        viewModel.selectedDate = date
                        showDayDetail = true
                    },
                    selectedDate: viewModel.selectedDate
                )
                .padding(.horizontal, ZQSpacing.lg)

                if let selectedDate = viewModel.selectedDate {
                    selectedDateDetail(for: selectedDate)
                }

                Spacer()
            }
            .background(Color.canvasParchment)
            .navigationTitle("日历")
            .navigationBarTitleDisplayMode(.inline)
            .onAppear { viewModel.loadData(context: modelContext) }
            .sheet(isPresented: $showDayDetail) {
                if let selectedDate = viewModel.selectedDate {
                    dayDetailSheet(date: selectedDate)
                }
            }
        }
    }

    private func selectedDateDetail(for date: Date) -> some View {
        let checkIns = viewModel.checkInsForDate(date)
        let activeItems = viewModel.trackItems.filter { $0.isActive }
        let completed = checkIns.filter { $0.status }.count

        return VStack(alignment: .leading, spacing: ZQSpacing.sm) {
            HStack {
                Text(dateStr(date))
                    .font(.zqBodyBold)
                Spacer()
                Text("\(completed)/\(activeItems.count) 完成")
                    .font(.zqCaption)
            }

            ForEach(checkIns, id: \.id) { checkIn in
                HStack {
                    Image(systemName: checkIn.status ? "checkmark.circle.fill" : "circle")
                        .foregroundColor(checkIn.status ? .checkGreen : .hairline)
                    Text(checkIn.trackItem?.name ?? "")
                        .font(.zqBody)
                    Spacer()
                }
            }
        }
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(Color.canvas)
        )
        .padding(.horizontal, ZQSpacing.lg)
        .padding(.top, ZQSpacing.md)
    }

    private func dayDetailSheet(date: Date) -> some View {
        let checkIns = viewModel.checkInsForDate(date)
        let activeItems = viewModel.trackItems.filter { $0.isActive }
        let completed = checkIns.filter { $0.status }.count

        return NavigationStack {
            VStack(alignment: .leading, spacing: ZQSpacing.md) {
                Text(dateStr(date))
                    .font(.zqTitle)
                    .padding(.horizontal, ZQSpacing.lg)

                HStack(spacing: ZQSpacing.xs) {
                    ProgressRing(
                        progress: activeItems.isEmpty ? 0 : Double(completed) / Double(activeItems.count),
                        lineWidth: 6,
                        ringColor: completed == activeItems.count ? .checkGreen : .checkYellow
                    )
                    .frame(width: 60, height: 60)

                    VStack(alignment: .leading) {
                        Text("完成率")
                            .font(.zqSubtitle)
                        Text("\(completed)/\(activeItems.count)")
                            .font(.zqTitle2)
                    }
                }
                .padding(.horizontal, ZQSpacing.lg)

                Divider().background(Color.hairline)

                VStack(spacing: 0) {
                    ForEach(Array(activeItems.enumerated()), id: \.element.id) { index, item in
                        let ci = checkIns.first(where: { $0.trackItem?.id == item.id })
                        ZhengQiTile(isAlternate: index % 2 == 1) {
                            HStack {
                                Image(systemName: item.iconName)
                                    .foregroundColor(ci?.status == true ? .primaryBlue : .inkMuted48)
                                Text(item.name)
                                    .font(.zqBody)
                                Spacer()
                                Image(systemName: ci?.status == true ? "checkmark.circle.fill" : "circle")
                                    .foregroundColor(ci?.status == true ? .checkGreen : .hairline)
                                    .font(.system(size: 22))
                            }
                        }
                    }
                }
            }
            .background(Color.canvasParchment)
            .navigationTitle("打卡详情")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("关闭") { showDayDetail = false }
                }
            }
        }
        .presentationDetents([.medium, .large])
    }

    private func dateStr(_ date: Date) -> String {
        let f = DateFormatter()
        f.dateFormat = "yyyy年M月d日 EEEE"
        f.locale = Locale(identifier: "zh_CN")
        return f.string(from: date)
    }
}