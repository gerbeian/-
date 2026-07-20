import SwiftUI

struct ProgressRing: View {
    let progress: Double
    let lineWidth: CGFloat
    var ringColor: Color = .primaryBlue
    var backgroundColor: Color = .hairline

    var body: some View {
        ZStack {
            Circle()
                .stroke(backgroundColor, lineWidth: lineWidth)
            Circle()
                .trim(from: 0, to: min(progress, 1.0))
                .stroke(ringColor, style: StrokeStyle(lineWidth: lineWidth, lineCap: .round))
                .rotationEffect(.degrees(-90))
                .animation(.spring(response: 0.5, dampingFraction: 0.7), value: progress)
        }
    }
}

struct AnimatedNumber: View {
    let value: Int
    var font: Font = .zqLargeNumber
    var color: Color = .ink

    @State private var displayValue: Int = 0

    var body: some View {
        Text("\(displayValue)")
            .font(font)
            .foregroundColor(color)
            .contentTransition(.numericText())
            .onAppear {
                displayValue = value
            }
            .onChange(of: value) { _, newValue in
                withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                    displayValue = newValue
                }
            }
    }
}