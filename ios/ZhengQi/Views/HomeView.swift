import SwiftUI
import SwiftData

struct HomeView: View {
    @Environment(\.modelContext) private var modelContext
    @State private var viewModel = HomeViewModel()
    @State private var showStreakAnimation = false

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: ZQSpacing.lg) {
                    streakHeader
                    zhengQiScoreCard
                    todayCheckList
                    if viewModel.dailyQuote != nil {
                        dailyQuoteCard
                    }
                    Spacer().frame(height: ZQSpacing.section)
                }
                .padding(.horizontal, ZQSpacing.lg)
            }
            .background(Color.canvasParchment)
            .navigationTitle("正气")
            .navigationBarTitleDisplayMode(.large)
            .onAppear {
                viewModel.loadData(context: modelContext)
                withAnimation(.spring(response: 0.5, dampingFraction: 0.7).delay(0.3)) {
                    showStreakAnimation = true
                }
            }
            .refreshable {
                viewModel.loadData(context: modelContext)
            }
        }
    }

    private var streakHeader: some View {
        VStack(spacing: ZQSpacing.xs) {
            ZStack {
                ProgressRing(
                    progress: viewModel.todayCompletionRate,
                    lineWidth: 8,
                    ringColor: .primaryBlue,
                    backgroundColor: .hairline
                )
                .frame(width: 120, height: 120)
                .opacity(showStreakAnimation ? 1 : 0)
                .scaleEffect(showStreakAnimation ? 1 : 0.5)

                VStack(spacing: 0) {
                    AnimatedNumber(value: viewModel.consecutiveDays, font: .zqLargeNumber)
                    Text("连续打卡")
                        .font(.zqSubtitle)
                }
            }

            Text("总打卡 \(viewModel.totalCheckInDays) 天")
                .font(.zqCaption)
        }
        .padding(.top, ZQSpacing.md)
    }

    private var zhengQiScoreCard: some View {
        VStack(spacing: ZQSpacing.sm) {
            HStack {
                Image(systemName: "bolt.fill")
                    .foregroundColor(.primaryBlue)
                Text("正气值")
                    .font(.zqSubtitle)
                Spacer()
                Text(ZhengQiCalculator.getLevel(score: viewModel.zhengQiScore).name)
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundColor(.primaryBlue)
                    .padding(.horizontal, ZQSpacing.sm)
                    .padding(.vertical, ZQSpacing.xxs)
                    .background(
                        RoundedRectangle(cornerRadius: ZQRounded.pill)
                            .fill(Color.primaryBlue.opacity(0.1))
                    )
            }

            AnimatedNumber(value: viewModel.zhengQiScore, font: .zqMediumNumber, color: .primaryBlue)
        }
        .padding(ZQSpacing.lg)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(.ultraThinMaterial)
        )
    }

    private var todayCheckList: some View {
        VStack(alignment: .leading, spacing: ZQSpacing.md) {
            Text("今日打卡")
                .font(.zqTitle2)

            if viewModel.trackItems.isEmpty {
                emptyStateView
            } else {
                VStack(spacing: 0) {
                    ForEach(Array(viewModel.trackItems.filter { $0.isActive }.enumerated()), id: \.element.id) { index, item in
                        ZhengQiTile(isAlternate: index % 2 == 1) {
                            checkInRow(for: item)
                        }
                    }
                }
                .clipShape(RoundedRectangle(cornerRadius: ZQRounded.lg))
            }
        }
    }

    private func checkInRow(for item: TrackItem) -> some View {
        let todayCheckIn = viewModel.todayCheckIns.first(where: { $0.trackItem?.id == item.id })
        let isChecked = todayCheckIn?.status ?? false

        return HStack(spacing: ZQSpacing.md) {
            Image(systemName: item.iconName)
                .font(.system(size: 22))
                .foregroundColor(isChecked ? .primaryBlue : .inkMuted48)
                .frame(width: 36)

            Text(item.name)
                .font(.zqBody)
                .foregroundColor(isChecked ? .primaryBlue : .ink)

            Spacer()

            Button {
                withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                    viewModel.toggleCheckIn(for: item, context: modelContext)
                }
            } label: {
                Image(systemName: isChecked ? "checkmark.circle.fill" : "circle")
                    .font(.system(size: 28))
                    .foregroundColor(isChecked ? .checkGreen : .hairline)
            }
        }
    }

    private var emptyStateView: some View {
        VStack(spacing: ZQSpacing.md) {
            Image(systemName: "list.bullet.clipboard")
                .font(.system(size: 40))
                .foregroundColor(.inkMuted48)
            Text("还没有打卡项目")
                .font(.zqBody)
                .foregroundColor(.inkMuted48)
            Text("前往「我的」添加打卡项目")
                .font(.zqCaption)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, ZQSpacing.xl)
        .background(
            RoundedRectangle(cornerRadius: ZQRounded.lg)
                .fill(Color.canvas)
        )
    }

    private var dailyQuoteCard: some View {
        let quote = viewModel.dailyQuote!
        return VStack(alignment: .leading, spacing: ZQSpacing.sm) {
            Text("每日一言")
                .font(.zqTitle2)

            QuoteCard(
                quote: quote,
                isFavorite: quote.isFavorite,
                onFavorite: {
                    withAnimation(.spring(response: 0.5, dampingFraction: 0.7)) {
                        quote.isFavorite.toggle()
                        try? modelContext.save()
                    }
                }
            )
        }
    }
}