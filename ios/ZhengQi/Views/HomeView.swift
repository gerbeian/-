import SwiftUI
import SwiftData
import PhotosUI
import UIKit

struct HomeView: View {
    @Environment(\.modelContext) private var modelContext
    @Binding var showEmergency: Bool
    @State private var viewModel = HomeViewModel()
    @State private var showStreakAnimation = false
    @State private var showBatchCheckIn = false
    @State private var showGoalDialog = false
    @State private var goalInput = ""
    @State private var recommendedArticle: Article?

    let manager = AbstinenceManager.shared
    var abstinenceDays: Int { manager.getCurrentStreak() }
    var stage: AbstinenceStage { manager.getStage() }
    var nextMilestone: (days: Int, label: String) { manager.getNextMilestone() }
    var goalDays: Int { manager.goalDays }
    var progress: Double {
        if goalDays > 0 {
            return min(Double(abstinenceDays) / Double(goalDays), 1.0)
        } else if nextMilestone.days > 0 {
            return min(Double(abstinenceDays) / Double(nextMilestone.days), 1.0)
        }
        return 0
    }

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Streak and Score section
                HStack(spacing: 0) {
                    ZStack {
                        ProgressRing(
                            progress: viewModel.todayCompletionRate,
                            lineWidth: 8,
                            ringColor: .primaryBlue,
                            backgroundColor: .hairline
                        )
                        .frame(width: 100, height: 100)
                        .opacity(showStreakAnimation ? 1 : 0)
                        .scaleEffect(showStreakAnimation ? 1 : 0.5)

                        VStack(spacing: 0) {
                            Text("\(viewModel.consecutiveDays)")
                                .font(.system(size: 24, weight: .bold))
                            Text("连续打卡")
                                .font(.system(size: 11))
                                .foregroundColor(.secondary)
                        }
                    }
                    .frame(maxWidth: .infinity)

                    // ZhengQi Score Card
                    VStack(spacing: 4) {
                        Text("正气值")
                            .font(.system(size: 12))
                            .foregroundColor(.white.opacity(0.85))
                        Text("\(viewModel.zhengQiScore)")
                            .font(.system(size: 36, weight: .bold))
                            .foregroundColor(.white)
                        Text("总打卡 \(viewModel.totalCheckInDays) 天")
                            .font(.system(size: 12))
                            .foregroundColor(.white.opacity(0.75))
                    }
                    .frame(width: 160)
                    .padding(20)
                    .background(
                        RoundedRectangle(cornerRadius: 16)
                            .fill(Color.primaryBlue)
                    )
                }

                // Abstinence Card
                AbstinenceCardView(
                    days: abstinenceDays,
                    stageLabel: stage.label,
                    stageDesc: stage.description,
                    nextMilestone: nextMilestone,
                    goalDays: goalDays,
                    progress: progress,
                    onEmergency: { showEmergency = true },
                    onSetGoal: {
                        goalInput = goalDays > 0 ? "\(goalDays)" : ""
                        showGoalDialog = true
                    }
                )

                // Today's check-in list
                VStack(alignment: .leading, spacing: 12) {
                    HStack {
                        Text("今日打卡")
                            .font(.system(size: 20, weight: .semibold))
                        Spacer()
                        Button {
                            showBatchCheckIn = true
                        } label: {
                            HStack(spacing: 6) {
                                Image(systemName: "pencil")
                                    .font(.system(size: 14))
                                Text("打卡")
                                    .font(.system(size: 14, weight: .semibold))
                            }
                            .padding(.horizontal, 20)
                            .padding(.vertical, 8)
                            .background(Color.primaryBlue)
                            .foregroundColor(.white)
                            .cornerRadius(20)
                        }
                    }

                    ForEach(viewModel.trackItems.filter { $0.isActive }) { item in
                        CheckInRowView(
                            item: item,
                            isChecked: viewModel.todayCheckIns.contains(where: { $0.trackItem?.id == item.id && $0.status }),
                            onTap: { showBatchCheckIn = true }
                        )
                    }
                }

                // Stats row
                HStack(spacing: 12) {
                    StatCardView(title: "完成率", value: "\(Int(viewModel.todayCompletionRate * 100))%", icon: "checkmark.circle.fill", color: .checkGreen)
                    StatCardView(title: "总天数", value: "\(viewModel.totalCheckInDays)", icon: "calendar", color: .primaryBlue)
                    StatCardView(title: "连续", value: "\(viewModel.consecutiveDays)天", icon: "flame.fill", color: .orange)
                }

                // Daily Quote
                if let quote = viewModel.dailyQuote {
                    QuoteCard(quote: quote, isFavorite: quote.isFavorite, onFavorite: {
                        quote.isFavorite.toggle()
                        try? modelContext.save()
                    })
                }

                // Recommended article link
                if let article = recommendedArticle {
                    NavigationLink(destination: ArticleDetailView(article: article)) {
                        HStack {
                            Image(systemName: "book.fill")
                                .foregroundColor(.primaryBlue)
                                .font(.system(size: 20))
                            VStack(alignment: .leading, spacing: 2) {
                                Text("推荐文章")
                                    .font(.system(size: 15, weight: .semibold))
                                Text("点击查看最新修心文章")
                                    .font(.system(size: 12))
                                    .foregroundColor(.secondary)
                            }
                            Spacer()
                            Image(systemName: "chevron.right")
                                .foregroundColor(.secondary)
                        }
                        .padding(17)
                        .background(
                            RoundedRectangle(cornerRadius: 16)
                                .fill(.ultraThinMaterial)
                        )
                    }
                    .buttonStyle(.plain)
                }

                Spacer().frame(height: 80)
            }
            .padding(.horizontal, 17)
        }
        .background(Color.canvasParchment)
        .navigationTitle("正气")
        .navigationBarTitleDisplayMode(.large)
        .onAppear {
            viewModel.loadData(context: modelContext)
            let articleDesc = FetchDescriptor<Article>(sortBy: [SortDescriptor(\.createdAt, order: .reverse)])
            recommendedArticle = (try? modelContext.fetch(articleDesc))?.first
            withAnimation(.spring(response: 0.5, dampingFraction: 0.7).delay(0.3)) {
                showStreakAnimation = true
            }
        }
        .refreshable {
            viewModel.loadData(context: modelContext)
        }
        .sheet(isPresented: $showBatchCheckIn) {
            BatchCheckInSheet(
                trackItems: viewModel.trackItems.filter { $0.isActive },
                onConfirm: { checkedIds, note, imageUri in
                    viewModel.batchCheckIn(trackItemIds: checkedIds, note: note, imageUri: imageUri, context: modelContext)
                }
            )
        }
        .alert("设置戒色目标", isPresented: $showGoalDialog) {
            TextField("目标天数", text: $goalInput)
                .keyboardType(.numberPad)
            Button("确定") {
                if let days = Int(goalInput) {
                    manager.goalDays = days
                }
            }
            Button("取消", role: .cancel) {}
        } message: {
            Text("设定一个目标天数，帮助你更有动力地坚持戒色。")
        }
    }
}

// MARK: - AbstinenceCardView
struct AbstinenceCardView: View {
    let days: Int
    let stageLabel: String
    let stageDesc: String
    let nextMilestone: (days: Int, label: String)
    let goalDays: Int
    let progress: Double
    let onEmergency: () -> Void
    let onSetGoal: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text("戒色天数")
                        .font(.system(size: 20, weight: .semibold))
                    Text(stageDesc)
                        .font(.system(size: 12))
                        .foregroundColor(.secondary)
                }
                Spacer()
                Button {
                    onEmergency()
                } label: {
                    HStack(spacing: 6) {
                        Image(systemName: "shield.fill")
                            .font(.system(size: 14))
                        Text("应急")
                            .font(.system(size: 14, weight: .semibold))
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 8)
                    .background(Color.primaryBlue)
                    .foregroundColor(.white)
                    .cornerRadius(20)
                }
            }

            HStack(alignment: .bottom) {
                Text("\(days)")
                    .font(.system(size: 48, weight: .bold))
                Text("天")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundColor(.secondary)
                    .padding(.bottom, 6)
                Spacer()
                VStack(alignment: .trailing, spacing: 2) {
                    Text(stageLabel)
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundColor(.primaryBlue)
                    if goalDays > 0 {
                        Text("目标 \(goalDays)天 · 已完成 \(Int(progress * 100))%")
                            .font(.system(size: 12))
                            .foregroundColor(.secondary)
                    } else if nextMilestone.days > 0 {
                        Text("距离「\(nextMilestone.label)」\(nextMilestone.days - days)天")
                            .font(.system(size: 12))
                            .foregroundColor(.secondary)
                    }
                    Button {
                        onSetGoal()
                    } label: {
                        Text(goalDays > 0 ? "修改目标" : "设置目标")
                            .font(.system(size: 12))
                            .foregroundColor(.primaryBlue)
                    }
                }
            }

            if nextMilestone.days > 0 || goalDays > 0 {
                ProgressView(value: progress)
                    .tint(.primaryBlue)
                    .background(Color.primaryBlue.opacity(0.12))
                    .frame(height: 4)
            }
        }
        .padding(20)
        .background(
            RoundedRectangle(cornerRadius: 16)
                .fill(.ultraThinMaterial)
        )
    }
}

// MARK: - CheckInRowView
struct CheckInRowView: View {
    let item: TrackItem
    let isChecked: Bool
    let onTap: () -> Void
    @State private var isPressed = false

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: item.iconName)
                .font(.system(size: 20))
                .foregroundColor(isChecked ? .primaryBlue : .secondary)
                .frame(width: 24)

            Text(item.name)
                .font(.system(size: 16, weight: .medium))
                .foregroundColor(isChecked ? .primaryBlue : .primary)

            Spacer()

            ZStack {
                RoundedRectangle(cornerRadius: 6)
                    .fill(isChecked ? Color.primaryBlue : Color.hairline)
                    .frame(width: 28, height: 28)
                if isChecked {
                    Image(systemName: "checkmark")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundColor(.white)
                }
            }
        }
        .padding(.horizontal, 17)
        .padding(.vertical, 14)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(isChecked ? Color.primaryBlue.opacity(0.08) : Color.canvas)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(isChecked ? Color.clear : Color.hairline, lineWidth: 1)
        )
        .scaleEffect(isPressed ? 0.98 : 1)
        .animation(.spring(response: 0.3, dampingFraction: 0.6), value: isPressed)
        .onTapGesture {
            onTap()
        }
        .pressAction {
            withAnimation { isPressed = true }
        } onRelease: {
            withAnimation { isPressed = false }
        }
    }
}

// MARK: - StatCardView
struct StatCardView: View {
    let title: String
    let value: String
    let icon: String
    let color: Color

    var body: some View {
        VStack(spacing: 4) {
            Image(systemName: icon)
                .foregroundColor(color)
                .font(.system(size: 18))
            Text(value)
                .font(.system(size: 18, weight: .bold))
            Text(title)
                .font(.system(size: 12))
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
        .padding(12)
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(.ultraThinMaterial)
        )
    }
}

// MARK: - BatchCheckInSheet
struct BatchCheckInSheet: View {
    let trackItems: [TrackItem]
    let onConfirm: ([UUID], String, String) -> Void
    @Environment(\.dismiss) private var dismiss
    @State private var checkedIds = Set<UUID>()
    @State private var note = ""
    @State private var selectedImage: PhotosPickerItem?
    @State private var selectedImageData: Data?

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 0) {
                    ForEach(trackItems) { item in
                        let isChecked = checkedIds.contains(item.id)
                        HStack(spacing: 8) {
                            Button {
                                if isChecked {
                                    checkedIds.remove(item.id)
                                } else {
                                    checkedIds.insert(item.id)
                                }
                            } label: {
                                Image(systemName: isChecked ? "checkmark.square.fill" : "square")
                                    .foregroundColor(isChecked ? .primaryBlue : .secondary)
                                    .font(.system(size: 22))
                            }

                            Image(systemName: item.iconName)
                                .foregroundColor(isChecked ? .primaryBlue : .secondary)
                                .font(.system(size: 18))

                            Text(item.name)
                                .font(.system(size: 16))
                                .foregroundColor(isChecked ? .primaryBlue : .primary)

                            Spacer()
                        }
                        .padding(.vertical, 8)
                    }

                    Divider().padding(.vertical, 12)

                    if let data = selectedImageData, let uiImage = UIImage(data: data) {
                        ZStack(alignment: .topTrailing) {
                            Image(uiImage: uiImage)
                                .resizable()
                                .scaledToFill()
                                .frame(height: 160)
                                .clipShape(RoundedRectangle(cornerRadius: 12))

                            Button {
                                selectedImage = nil
                                selectedImageData = nil
                            } label: {
                                Image(systemName: "xmark.circle.fill")
                                    .font(.system(size: 22))
                                    .foregroundColor(.white)
                                    .background(Circle().fill(Color.black.opacity(0.5)))
                            }
                            .padding(4)
                        }
                        .padding(.bottom, 8)
                    }

                    PhotosPicker(selection: $selectedImage, matching: .images) {
                        HStack {
                            Image(systemName: "photo.badge.plus")
                                .font(.system(size: 16))
                            Text(selectedImageData != nil ? "更换图片" : "添加图片（可选）")
                        }
                        .frame(maxWidth: .infinity)
                        .padding(12)
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(Color.hairline, lineWidth: 1)
                        )
                    }
                    .onChange(of: selectedImage) { _, newValue in
                        Task {
                            if let data = try? await newValue?.loadTransferable(type: Data.self) {
                                selectedImageData = data
                            }
                        }
                    }
                    .padding(.bottom, 12)

                    TextField("备注（可选）", text: $note, axis: .vertical)
                        .font(.system(size: 15))
                        .padding(12)
                        .background(
                            RoundedRectangle(cornerRadius: 10)
                                .stroke(Color.hairline, lineWidth: 1)
                        )
                        .lineLimit(3)
                }
                .padding()
            }
            .navigationTitle("今日打卡")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button("取消") { dismiss() }
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button("打卡") {
                        if !checkedIds.isEmpty {
                            // Save image to temp file
                            var imageUri = ""
                            if let data = selectedImageData {
                                let tempDir = FileManager.default.temporaryDirectory
                                let fileName = "checkin_\(UUID().uuidString).jpg"
                                let fileURL = tempDir.appendingPathComponent(fileName)
                                try? data.write(to: fileURL)
                                imageUri = fileURL.path
                            }
                            onConfirm(Array(checkedIds), note, imageUri)
                        }
                        dismiss()
                    }
                    .fontWeight(.semibold)
                }
            }
        }
    }
}

// MARK: - Press Action Helper
struct PressActions: ViewModifier {
    var onPress: () -> Void
    var onRelease: () -> Void

    func body(content: Content) -> some View {
        content
            .simultaneousGesture(
                DragGesture(minimumDistance: 0)
                    .onChanged { _ in onPress() }
                    .onEnded { _ in onRelease() }
            )
    }
}

extension View {
    func pressAction(onPress: @escaping () -> Void, onRelease: @escaping () -> Void) -> some View {
        modifier(PressActions(onPress: onPress, onRelease: onRelease))
    }
}