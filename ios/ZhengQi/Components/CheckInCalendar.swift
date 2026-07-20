import SwiftUI

struct CheckInCalendar: View {
    let days: [Date]
    let dayStatus: (Date) -> DayStatus
    let onDayTap: (Date) -> Void
    let selectedDate: Date?

    private let columns = Array(repeating: GridItem(.flexible(), spacing: ZQSpacing.xxs), count: 7)
    private let weekDays = ["日", "一", "二", "三", "四", "五", "六"]

    var body: some View {
        VStack(spacing: ZQSpacing.sm) {
            HStack {
                ForEach(weekDays, id: \.self) { day in
                    Text(day)
                        .font(.zqSubtitle)
                        .frame(maxWidth: .infinity)
                }
            }
            LazyVGrid(columns: columns, spacing: ZQSpacing.xxs) {
                ForEach(Array(days.enumerated()), id: \.offset) { index, date in
                    if date == Date.distantPast {
                        Color.clear
                            .frame(height: 36)
                    } else {
                        DayCell(
                            date: date,
                            status: dayStatus(date),
                            isSelected: selectedDate.map { Calendar.current.isDate($0, inSameDayAs: date) } ?? false
                        )
                        .onTapGesture { onDayTap(date) }
                    }
                }
            }
        }
    }
}

struct DayCell: View {
    let date: Date
    let status: DayStatus
    let isSelected: Bool

    private let calendar = Calendar.current

    var body: some View {
        let day = calendar.component(.day, from: date)
        let isToday = calendar.isDateInToday(date)

        ZStack {
            if isSelected {
                RoundedRectangle(cornerRadius: ZQRounded.sm)
                    .fill(Color.primaryBlue.opacity(0.15))
            }
            if isToday && !isSelected {
                RoundedRectangle(cornerRadius: ZQRounded.sm)
                    .stroke(Color.primaryBlue, lineWidth: 1.5)
            }

            RoundedRectangle(cornerRadius: ZQRounded.xs)
                .fill(statusColor)
                .frame(width: 28, height: 28)

            Text("\(day)")
                .font(.system(size: 13, weight: isToday ? .bold : .regular))
                .foregroundColor(isToday ? .primaryBlue : .ink)
        }
        .frame(height: 36)
        .animation(.spring(response: 0.5, dampingFraction: 0.7), value: status)
    }

    var statusColor: Color {
        switch status {
        case .full: return .checkGreen
        case .partial: return .checkYellow
        case .none: return .checkGray
        }
    }
}